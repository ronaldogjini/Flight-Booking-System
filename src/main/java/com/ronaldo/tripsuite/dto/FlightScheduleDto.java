package com.ronaldo.tripsuite.dto;

import com.ronaldo.tripsuite.entity.Flight;
import com.ronaldo.tripsuite.entity.Plane;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
public class FlightScheduleDto {
        private Long flightId;
        private Date departureDate;
        private Date arrivalDate;
        private Time departureTime;
        private Time arrivalTime;
        private String planeId;
}
