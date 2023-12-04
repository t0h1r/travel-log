package uz.travellog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelLog {

    private long id;
    private Date date;
    private String vehicleRegNumber;
    private String vehicleOwnerName;
    private int odometerStart;
    private int odometerEnd;
    private String route;
    private String description;
    private Date createdAt;
    private Date updatedAt;

}
