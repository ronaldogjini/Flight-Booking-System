package com.ronaldo.tripsuite.controller;

import com.ronaldo.tripsuite.dto.UserDto;
import com.ronaldo.tripsuite.entity.User;
import com.ronaldo.tripsuite.mapper.UserMapper;
import com.ronaldo.tripsuite.service.UserService;
import com.ronaldo.tripsuite.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    UserMapper userMapper;

    // as an admin, I create new users
    @PreAuthorize("hasRole('Admin')")
    @PostMapping({"/users"})
    public ResponseEntity<UserDto> registerNewUser(@RequestBody UserDto userDto) {
        UserDto savedUser = userService.saveUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


}
