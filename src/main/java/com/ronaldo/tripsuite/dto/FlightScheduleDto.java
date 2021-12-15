package com.ronaldo.tripsuite.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ronaldo.tripsuite.entity.Flight;
import com.ronaldo.tripsuite.entity.Plane;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Date;

@Getter
@Setter
public class FlightScheduleDto {
    private Long id;
    @NotNull(message = "Flight ID cannot be null")
    private Long flightId;

    private Flight flight;
    private Plane plane;

    @NotNull(message = "Departure date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date departureDate;
    @NotNull(message = "Arrival Date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date arrivalDate;
    @NotNull(message = "Plane ID cannot be null")
    private String planeId;
}
