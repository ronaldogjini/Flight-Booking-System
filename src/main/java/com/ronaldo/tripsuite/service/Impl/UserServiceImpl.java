package com.ronaldo.tripsuite.service.Impl;

import com.ronaldo.tripsuite.dto.UserDto;
import com.ronaldo.tripsuite.mapper.UserMapper;
import com.ronaldo.tripsuite.repository.RoleRepository;
import com.ronaldo.tripsuite.repository.UserRepository;
import com.ronaldo.tripsuite.entity.Role;
import com.ronaldo.tripsuite.entity.User;
import com.ronaldo.tripsuite.service.RoleService;
import com.ronaldo.tripsuite.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserMapper userMapper;

    public UserDto saveUser(UserDto userDto) {

        User newUser = userMapper.dtoToUser(userDto);
        Role role = roleService.getByName("User");
        Set<Role> userRoles = new HashSet<>();

        userRoles.add(role);
        newUser.setRole(userRoles);
        newUser.setPassword(getEncodedPassword(newUser.getPassword()));
        User savedUser = userRepository.save(newUser);

        log.info("New user created");
        return userMapper.userToDto(savedUser);
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }


    @Override
    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User does not exist");
        }

        log.info("User fetched by username!");
        return userOptional.get();
    }


}
