package com.ronaldo.tripsuite.dto;

import com.ronaldo.tripsuite.enums.TripStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TripStatusChangeDto {
    @NotNull(message = "Trip ID cannot be null")
    private Long id;
    @NotNull(message = "Trip status cannot be null")
    private TripStatus status;

}
