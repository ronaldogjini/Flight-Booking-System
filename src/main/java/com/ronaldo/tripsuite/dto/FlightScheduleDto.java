package com.ronaldo.tripsuite.dto;

import com.ronaldo.tripsuite.entity.Flight;
import com.ronaldo.tripsuite.entity.Plane;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
public class FlightScheduleDto {
    @NotNull(message = "Flight ID cannot be null")
    private Long flightId;
    @NotNull(message = "Departure date cannot be null")
    private Date departureDate;
    @NotNull(message = "Arrival Date cannot be null")
    private Date arrivalDate;
    @NotNull(message = "Plane ID cannot be null")
    private String planeId;
}
