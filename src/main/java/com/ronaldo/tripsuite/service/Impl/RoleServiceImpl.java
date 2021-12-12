package com.ronaldo.tripsuite.service.Impl;

import com.ronaldo.tripsuite.dto.RoleDto;
import com.ronaldo.tripsuite.mapper.RoleMapper;
import com.ronaldo.tripsuite.repository.RoleRepository;
import com.ronaldo.tripsuite.entity.Role;
import com.ronaldo.tripsuite.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    public RoleDto saveRole(RoleDto roleDto) {

        Role newRole = roleMapper.dtoToRole(roleDto);
        Role savedRole = roleRepository.save(newRole);
        return roleMapper.roleToDto(savedRole);
    }

    public Role getByName(String name) {
        return roleRepository.getByName(name);
    }
}
