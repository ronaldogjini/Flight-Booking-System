package com.ronaldo.tripsuite.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "flights")
@Getter
@Setter
public class Flight {

    @Id
    private Long id;

    @Column(name = "number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "departure_airport", referencedColumnName = "id")
    private Airport departureAirport;

    @ManyToOne
    @JoinColumn(name = "destination_airport", referencedColumnName = "id")
    private Airport destinationAirport;

}
