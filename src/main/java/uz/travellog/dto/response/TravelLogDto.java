package uz.travellog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.travellog.model.TravelLog;
import uz.travellog.util.DateUtil;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TravelLogDto {

    private Long id;
    private String date;
    private String vehicleRegNumber;
    private String vehicleOwnerName;
    private Integer odometerStart;
    private Integer odometerEnd;
    private String route;
    private String description;
    private String createdAt;
    private String updatedAt;

    public static TravelLogDto of(TravelLog model) {
        return TravelLogDto
                .builder()
                .id(model.getId())
                .date(model.getDate().toString())
                .vehicleRegNumber(model.getVehicleRegNumber())
                .vehicleOwnerName(model.getVehicleOwnerName())
                .odometerStart(model.getOdometerStart())
                .odometerEnd(model.getOdometerEnd())
                .route(model.getRoute())
                .description(model.getDescription())
                .createdAt(DateUtil.formatDateToString(model.getCreatedAt()))
                .updatedAt(DateUtil.formatDateToString(model.getUpdatedAt()))
                .build();
    }
}
