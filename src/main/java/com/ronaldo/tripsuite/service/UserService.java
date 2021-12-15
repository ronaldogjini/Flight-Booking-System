package com.ronaldo.tripsuite.service;

import com.ronaldo.tripsuite.dto.UserDto;
import com.ronaldo.tripsuite.entity.User;

public interface UserService {

    UserDto saveUser(UserDto userDto);

    User findByUsername(String username);
}
