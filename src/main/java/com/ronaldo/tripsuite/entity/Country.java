package com.ronaldo.tripsuite.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "countries")
@Getter
@Setter

public class Country {

    @NotBlank
    @Id
    private String id;

    @NotBlank
    @Column(name = "name")
    private String name;


}
