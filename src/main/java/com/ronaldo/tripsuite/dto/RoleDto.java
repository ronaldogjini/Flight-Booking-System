package com.ronaldo.tripsuite.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RoleDto {

    @NotNull(message = "Name cannot be null")
    private String name;
    @NotNull(message = "Description cannot be null")
    private String description;
}
