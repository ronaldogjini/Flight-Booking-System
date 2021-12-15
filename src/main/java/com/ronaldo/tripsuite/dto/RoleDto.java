package com.ronaldo.tripsuite.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;


@Getter
@Setter
public class RoleDto {

    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotEmpty(message = "Description cannot be empty")
    private String description;
}
