package com.ronaldo.tripsuite.controller;

import com.ronaldo.tripsuite.dto.JwtRequestDto;
import com.ronaldo.tripsuite.dto.JwtResponseDto;
import com.ronaldo.tripsuite.service.Impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JwtController {

    @Autowired
    private AuthService authService;

    @PostMapping("/api/login")
    public JwtResponseDto createJwtToken(@RequestBody JwtRequestDto jwtRequestDto) throws Exception {
        System.out.println("Authenticate Controller");
        return authService.createJwtToken(jwtRequestDto);
    }

}
