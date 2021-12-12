package com.ronaldo.tripsuite.dto;

import com.ronaldo.tripsuite.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponseDto {

    private User user;
    private String jwtToken;

    public JwtResponseDto(User user, String jwtToken) {
        this.user = user;
        this.jwtToken = jwtToken;
    }
}
