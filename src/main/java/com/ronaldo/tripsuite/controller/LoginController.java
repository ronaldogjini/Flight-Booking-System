package com.ronaldo.tripsuite.controller;

import com.ronaldo.tripsuite.dto.JwtRequestDto;
import com.ronaldo.tripsuite.dto.JwtResponseDto;
import com.ronaldo.tripsuite.service.Impl.AuthService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")

public class LoginController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @ApiOperation(value = "Login point to the app")
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Wrong credentials")

    })
    public JwtResponseDto createJwtToken(@RequestBody JwtRequestDto jwtRequestDto) throws Exception {
        System.out.println("Authenticate Controller");
        return authService.createJwtToken(jwtRequestDto);
    }

}
