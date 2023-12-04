package uz.travellog.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.travellog.dto.request.TravelLogRequest;
import uz.travellog.model.TravelLog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class TravelLogRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TravelLogRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Mono<Long> insertTravelLog(TravelLogRequest travelLog) {
        String sql = "INSERT INTO travel_log " +
                "(date, vehicle_registration_number, vehicle_owner_name, odometer_value_start, " +
                "odometer_value_end, route, journey_description, created_at, updated_at) " +
                "VALUES (:date, :vehicleRegNumber, :vehicleOwnerName, :odometerStart, " +
                ":odometerEnd, :route, :description, :createdAt, :updatedAt)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
        .addValue("date", travelLog.getDate())
        .addValue("vehicleRegNumber", travelLog.getVehicleRegNumber())
        .addValue("vehicleOwnerName", travelLog.getVehicleOwnerName())
        .addValue("odometerStart", travelLog.getOdometerStart())
        .addValue("odometerEnd", travelLog.getOdometerEnd())
        .addValue("route", travelLog.getRoute())
        .addValue("description", travelLog.getDescription())
        .addValue("createdAt", new Date())
        .addValue("updatedAt", new Date());
        return Mono.fromCallable(() -> {
            namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[] { "id" });
            if (keyHolder.getKey() != null) {
                return keyHolder.getKey().longValue();
            } else {
                return -1L;
            }
        });
    }


    public Mono<TravelLog> updateTravelLog(Long id, TravelLogRequest request) {

        String sql = """
                UPDATE travel_log SET date = :date, vehicle_registration_number = :vehicleRegNum, 
                vehicle_owner_name = :vehicleOwner, odometer_value_start = :odometerStart, 
                odometer_value_end = :odometerEnd, route = :route, journey_description = :journeyDesc, updated_at = :updateAt 
                WHERE id = :id RETURNING *;
                """;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("date", request.getDate());
        paramMap.put("vehicleRegNum", request.getVehicleRegNumber());
        paramMap.put("vehicleOwner", request.getVehicleOwnerName());
        paramMap.put("odometerStart", request.getOdometerStart());
        paramMap.put("odometerEnd", request.getOdometerEnd());
        paramMap.put("route", request.getRoute());
        paramMap.put("journeyDesc", request.getDescription());
        paramMap.put("updateAt", new Date());
        paramMap.put("id", id);

        return Mono.fromCallable(()->namedParameterJdbcTemplate.queryForObject(sql, paramMap, new TravelLogRowMapper()));
    }

    public Mono<TravelLog> getById(Long id) {

        String sql = "SELECT * FROM travel_log WHERE id = :id";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);

        return Mono.fromCallable(()->namedParameterJdbcTemplate.queryForObject(sql, paramMap, new TravelLogRowMapper()));
    }

    public Flux<TravelLog> getAll() {

        String sql = "select * from travel_log";

        return Flux.defer(() -> Flux.fromIterable(namedParameterJdbcTemplate.query(sql, Collections.emptyMap(), new TravelLogRowMapper())));

    }

    public Mono<Integer> delete(long id) {

        String sql = "DELETE FROM travel_log WHERE id = :id";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        return Mono.fromCallable(() ->
                namedParameterJdbcTemplate.update(sql, paramMap));

    }

    public Flux<TravelLog> getReport(Date startDate, Date endDate, String vehicleRegistrationNumber, String ownerName) {

        StringBuilder sql = new StringBuilder("SELECT * FROM travel_log WHERE 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (startDate != null) {
            sql.append(" AND date >= :startDate");
            params.put("startDate", startDate);
        }

        if (endDate != null) {
            sql.append(" AND date <= :endDate");
            params.put("endDate", endDate);
        }

        if (vehicleRegistrationNumber != null) {
            sql.append(" AND vehicle_registration_number = :vehicleRegNumber");
            params.put("vehicleRegNumber", vehicleRegistrationNumber);
        }

        if (ownerName != null) {
            sql.append(" AND vehicle_owner_name = :ownerName");
            params.put("ownerName", ownerName);
        }

        return Flux.defer(() -> Flux.fromIterable(namedParameterJdbcTemplate.query(sql.toString(), params, new TravelLogRowMapper())));
    }

    private static class TravelLogRowMapper implements RowMapper<TravelLog> {
        @Override
        public TravelLog mapRow(ResultSet rs, int rowNum) throws SQLException {
            return TravelLog.builder()
                    .id(rs.getLong("id"))
                    .date(rs.getDate("date"))
                    .vehicleRegNumber(rs.getString("vehicle_registration_number"))
                    .vehicleOwnerName(rs.getString("vehicle_owner_name"))
                    .odometerStart(rs.getInt("odometer_value_start"))
                    .odometerEnd(rs.getInt("odometer_value_end"))
                    .route(rs.getString("route"))
                    .description(rs.getString("journey_description"))
                    .updatedAt(rs.getTimestamp("updated_at"))
                    .createdAt(rs.getTimestamp("created_at"))
                    .build();
        }
    }

}
