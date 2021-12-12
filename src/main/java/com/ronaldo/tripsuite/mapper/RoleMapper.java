package com.ronaldo.tripsuite.mapper;

import com.ronaldo.tripsuite.dto.RoleDto;
import com.ronaldo.tripsuite.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {


    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDto roleToDto(Role role);
    Role dtoToRole(RoleDto roleDto);

}
