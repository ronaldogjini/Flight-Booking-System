package com.ronaldo.tripsuite.mapper;

import com.ronaldo.tripsuite.dto.UserDto;
import com.ronaldo.tripsuite.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(ignore = true, target = "password")
    UserDto userToDto(User user);

    User dtoToUser(UserDto userDto);
}
