package com.ronaldo.tripsuite.service;

import com.ronaldo.tripsuite.dto.UserDto;
import com.ronaldo.tripsuite.entity.User;

import java.util.List;

public interface UserService {

    UserDto saveUser(UserDto userDto);
    User findByUsername(String username);
}
