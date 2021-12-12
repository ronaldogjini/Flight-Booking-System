package com.ronaldo.tripsuite.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ronaldo.tripsuite.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String username;
    private String name;
    private String password;
    private String email;
    private Set<Role> role;
}
