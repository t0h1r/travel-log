package uz.travellog.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.travellog.dto.response.TravelLogDto;
import uz.travellog.dto.response.TravelLogReportDto;
import uz.travellog.model.TravelLog;
import uz.travellog.repository.TravelLogRepository;
import uz.travellog.dto.request.TravelLogRequest;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class TravelLogService {

    private final TravelLogRepository travelLogRepository;

    public TravelLogService(TravelLogRepository travelLogRepository) {
        this.travelLogRepository = travelLogRepository;
    }

    public Mono<Long> addNewTravelLog(TravelLogRequest request) {
        return travelLogRepository.insertTravelLog(request);
    }


    public Mono<TravelLog> updateLog(long id, TravelLogRequest request) {
        return travelLogRepository.updateTravelLog(id, request);
    }

    public Mono<TravelLog> getById(long id) {
        return travelLogRepository.getById(id);
    }

    public Mono<Integer> delete(long id) {
        return travelLogRepository.delete(id);
    }

    public List<TravelLogDto> getAll() {
        return travelLogRepository.getAll().toStream()
                .map(TravelLogDto::of)
                .toList();
    }

    public List<TravelLogReportDto> getReport(Date startDate, Date endDate, String vehicleRegistrationNumber, String ownerName) {

        Flux<TravelLog> report = travelLogRepository.getReport(startDate, endDate, vehicleRegistrationNumber, ownerName);
        List<TravelLog> reportList = report.collectList().block();

        Map<Date, List<TravelLog>> map = reportList
                .stream()
                .collect(Collectors.groupingBy(TravelLog::getDate));
        List<TravelLogReportDto> result = new ArrayList<>();


        for (Map.Entry<Date, List<TravelLog>> entry: map.entrySet()) {

            List<TravelLog> value = entry.getValue();
            int totalDistance = value.stream().mapToInt(model -> model.getOdometerEnd() - model.getOdometerStart()).sum();
            Date date = entry.getKey();

            TravelLogReportDto item = TravelLogReportDto
                    .builder()
                    .date(date)
                    .totalDistance(totalDistance)
                    .item(value)
                    .build();
            result.add(item);
        }
        return result;
    }
}
