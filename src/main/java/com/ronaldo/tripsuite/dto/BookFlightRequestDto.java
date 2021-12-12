package com.ronaldo.tripsuite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookFlightRequestDto {

    private Long tripId;
    private Long flightId;
}
