package com.ronaldo.tripsuite.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BookFlightRequestDto {

    @NotNull(message = " Trip ID cannot be null")
    private Long tripId;
    @NotNull(message = "Flight Schedule ID cannot be null")
    private Long flightId;
}
