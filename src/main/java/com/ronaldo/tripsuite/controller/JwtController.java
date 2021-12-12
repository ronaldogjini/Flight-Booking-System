package com.ronaldo.tripsuite.controller;

import com.ronaldo.tripsuite.dto.JwtRequestDto;
import com.ronaldo.tripsuite.dto.JwtResponseDto;
import com.ronaldo.tripsuite.service.Impl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JwtController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/api/login")
    public JwtResponseDto createJwtToken(@RequestBody JwtRequestDto jwtRequestDto) throws Exception {
        System.out.println("Authenticate Controller");
        return jwtService.createJwtToken(jwtRequestDto);
    }
//
//    @GetMapping({"/authenticated"})
//    public String createJwtToken() throws Exception {
//
//        return "Hello World";
//    }
}
