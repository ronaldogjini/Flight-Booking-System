package com.ronaldo.tripsuite.controller;

import com.ronaldo.tripsuite.dto.UserDto;
import com.ronaldo.tripsuite.mapper.UserMapper;
import com.ronaldo.tripsuite.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping({"/users"})
    @ApiOperation(value = "Add a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You do not have the proper permissions"),
            @ApiResponse(code = 401, message = "You are not authorized to do that")

    })
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserDto userDto) {
        UserDto savedUser = userService.saveUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


}
