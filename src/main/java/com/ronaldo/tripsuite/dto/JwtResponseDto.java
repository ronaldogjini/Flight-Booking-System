package com.ronaldo.tripsuite.dto;

import com.ronaldo.tripsuite.entity.User;
import com.ronaldo.tripsuite.mapper.UserMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

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
