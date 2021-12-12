package com.ronaldo.tripsuite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRequestDto {

    private String username;
    private String password;
}
