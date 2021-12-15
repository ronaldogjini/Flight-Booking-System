package com.ronaldo.tripsuite.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ronaldo.tripsuite.enums.TripReason;
import com.ronaldo.tripsuite.enums.TripStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Getter
@Setter
public class TripDto {

    private Long id;
    private Long userId;
    @NotNull(message = "Reason cannot be null")
    private TripReason reason;
    @NotBlank(message = "Description cannot be empty")
    private String description;
    @NotBlank(message = "Departure location cannot be empty")
    private String departureLocation;
    @NotBlank(message = "Arrival location cannot be empty")
    private String arrivalLocation;
    @NotNull(message = "Departure Date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date departureDate;
    @NotNull(message = "Arrival Date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date arrivalDate;

    private TripStatus status;
}
