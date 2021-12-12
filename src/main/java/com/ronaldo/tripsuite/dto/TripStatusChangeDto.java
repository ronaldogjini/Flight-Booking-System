package com.ronaldo.tripsuite.dto;

import com.ronaldo.tripsuite.enums.TripStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripStatusChangeDto {

    private Long id;
    private TripStatus status;

}
