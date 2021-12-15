package com.ronaldo.tripsuite.service.Impl;

import com.ronaldo.tripsuite.dto.RoleDto;
import com.ronaldo.tripsuite.mapper.RoleMapper;
import com.ronaldo.tripsuite.repository.RoleRepository;
import com.ronaldo.tripsuite.entity.Role;
import com.ronaldo.tripsuite.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public RoleDto saveRole(RoleDto roleDto) {

        Role newRole = roleMapper.dtoToRole(roleDto);
        Role savedRole = roleRepository.save(newRole);

        log.info("New role saved!");
        return roleMapper.roleToDto(savedRole);


    }

    public Role getByName(String name) {
        return roleRepository.getByName(name);
    }
}
