package uz.travellog.controller;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import uz.travellog.dto.request.TravelLogRequest;
import uz.travellog.dto.response.AddLogResponse;
import uz.travellog.dto.response.Response;
import uz.travellog.dto.response.TravelLogDto;
import uz.travellog.dto.response.TravelLogReportDto;
import uz.travellog.service.TravelLogService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/travel/log")
public class TravelLogController {

    private final TravelLogService service;

    public TravelLogController(TravelLogService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<ResponseEntity<Response<AddLogResponse>>> addNewLog(@RequestBody @Valid TravelLogRequest request) {
        return service.addNewTravelLog(request)
                .map(rowsAffected -> ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response<>(200, "SUCCESS",new AddLogResponse(rowsAffected))))
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response<>(-1, "Error occurred during insertion", null)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Response<TravelLogDto>>> updateLog(@PathVariable long id, @RequestBody TravelLogRequest request) {
        return service.updateLog(id, request)
                .map(rowsAffected -> ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response<>(200, "SUCCESS", TravelLogDto.of(rowsAffected))))
                .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(-1, "Object Not Found", null)));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Response<TravelLogDto>>> updateLog(@PathVariable long id) {
        return service.getById(id)
                .map(rowsAffected -> ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response<>(200, "SUCCESS", TravelLogDto.of(rowsAffected))))
                .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(-1, "Object Not Found", null)));
    }

    @GetMapping
    public Response<List<TravelLogDto>> getAll() {
        return new Response<>(200, "SUCCESS", service.getAll());
    }

   @GetMapping("/report")
    public Response<List<TravelLogReportDto>> getReport(@RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") Date startDate,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") Date endDate,
                                                        @RequestParam(required = false) String vehicleRegistrationNumber,
                                                        @RequestParam(required = false) String ownerName) {
        return new Response<>(200, "SUCCESS", service.getReport(startDate, endDate, vehicleRegistrationNumber, ownerName));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Response>> delete(@PathVariable long id) {
        return service.delete(id)
        .map(rowsAffected -> {
            if (rowsAffected > 0) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new Response<>(200, "SUCCESS", "Object deleted"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Response<>(-1, "Object Not Found", null));
            }
        });
    }
}
