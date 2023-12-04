package uz.travellog.dto.response;

import lombok.Builder;
import lombok.Data;
import uz.travellog.model.TravelLog;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class TravelLogReportDto {

    private Date date;
    private Integer totalDistance;
    private List<TravelLog> item;

}
