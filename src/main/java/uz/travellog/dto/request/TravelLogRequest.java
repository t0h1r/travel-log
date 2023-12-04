package uz.travellog.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class TravelLogRequest {

    private Date date;
    private String vehicleRegNumber;
    private String vehicleOwnerName;
    private Integer odometerStart;
    private Integer odometerEnd;
    private String route;
    private String description;

}
