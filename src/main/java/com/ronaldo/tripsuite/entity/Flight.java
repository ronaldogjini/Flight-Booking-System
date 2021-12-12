package com.ronaldo.tripsuite.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.exception.DataException;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "flights")
@Getter
@Setter
public class Flight {

    @Id
    private Long id;

    @NotBlank
    @Column(name = "number")
    private String number;

    @NotBlank
    @ManyToOne
    @JoinColumn(name="departure_airport", referencedColumnName = "id")
    private Airport departureAirport;

    @NotBlank
    @ManyToOne
    @JoinColumn(name="destination_airport", referencedColumnName = "id")
    private Airport destinationAirport;

}
