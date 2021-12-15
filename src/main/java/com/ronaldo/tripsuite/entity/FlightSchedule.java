package com.ronaldo.tripsuite.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "flight_schedules")
@Getter
@Setter
public class FlightSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "flight_id", referencedColumnName = "id")
    private Flight flight;

    @Column(name = "departure_date")
    private Date departureDate;

    @Column(name = "arrival_date")
    private Date arrivalDate;

    @ManyToOne
    @JoinColumn(name = "plane_id", referencedColumnName = "id")
    private Plane plane;
}
