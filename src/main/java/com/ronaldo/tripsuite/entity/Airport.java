package com.ronaldo.tripsuite.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "airports")
@Getter
@Setter
public class Airport {

    @Id
    @NotBlank
    private String id;

    @NotBlank
    @ManyToOne
    @JoinColumn(name="country_id", referencedColumnName = "id")
    private Country country;

    @NotBlank
    @Column(name = "city")
    private String city;
}
