package com.ronaldo.tripsuite.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
@Getter
@Setter
public class RoleDto {

    private String name;
    private String description;
}
