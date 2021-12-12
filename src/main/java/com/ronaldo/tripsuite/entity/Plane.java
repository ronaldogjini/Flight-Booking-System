package com.ronaldo.tripsuite.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "planes")
@Getter
@Setter
public class Plane {

    @Id
    private String id;

    @NotBlank
    @Column(name = "manufacturer")
    private String manufacturer;

    @NotBlank
    @Column(name = "make")
    private String make;
}
