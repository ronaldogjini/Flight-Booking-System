package com.ronaldo.tripsuite.service;

import com.ronaldo.tripsuite.dto.RoleDto;
import com.ronaldo.tripsuite.entity.Role;

public interface RoleService {

    RoleDto saveRole(RoleDto role);

    Role getByName(String name);
}
