package com.ronaldo.tripsuite.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ronaldo.tripsuite.enums.TripReason;
import com.ronaldo.tripsuite.enums.TripStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
public class TripDto {

    private Long id;
    private Long userId;
    private TripReason reason;
    private String description;
    private String departureLocation;
    private String arrivalLocation;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date departureDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date arrivalDate;
    private TripStatus status;
}
