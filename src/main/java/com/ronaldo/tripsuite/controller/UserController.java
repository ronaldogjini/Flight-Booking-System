package com.ronaldo.tripsuite.controller;

import com.ronaldo.tripsuite.dto.UserDto;
import com.ronaldo.tripsuite.entity.User;
import com.ronaldo.tripsuite.mapper.UserMapper;
import com.ronaldo.tripsuite.service.UserService;
import com.ronaldo.tripsuite.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    UserMapper userMapper;

    // as an admin, I create new users
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
