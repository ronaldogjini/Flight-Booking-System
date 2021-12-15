package com.ronaldo.tripsuite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponseDto {

    private UserDto userDto;
    private String jwtToken;

    public JwtResponseDto(UserDto userDto, String jwtToken) {
        this.userDto = userDto;
        this.jwtToken = jwtToken;
    }
}
