package com.ronaldo.tripsuite.dto;

import com.ronaldo.tripsuite.entity.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class UserDto {

    private Long id;
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Password cannot be null")
    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters")

    private String password;
    @NotNull(message = "Email cannot be null")
    private String email;
    private Set<Role> role;
}
