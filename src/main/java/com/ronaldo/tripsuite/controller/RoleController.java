package com.ronaldo.tripsuite.controller;

import com.ronaldo.tripsuite.dto.RoleDto;
import com.ronaldo.tripsuite.entity.Role;
import com.ronaldo.tripsuite.service.RoleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasRole('Admin')")
    @PostMapping({"/roles"})
    @ApiOperation(value = "Create a new user role")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You do not have the proper permissions"),
            @ApiResponse(code = 401, message = "You are not authorized to do that")

    })
    public ResponseEntity<RoleDto> saveRole(@Valid @RequestBody RoleDto roleDto) {
        RoleDto savedRole = roleService.saveRole(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
    }
}
