package com.ronaldo.tripsuite.controller;

import com.ronaldo.tripsuite.dto.RoleDto;
import com.ronaldo.tripsuite.entity.Role;
import com.ronaldo.tripsuite.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasRole('Admin')")
    @PostMapping({"/roles"})
    public RoleDto saveRole(@RequestBody RoleDto roleDto) {
        return roleService.saveRole(roleDto);
    }
}
