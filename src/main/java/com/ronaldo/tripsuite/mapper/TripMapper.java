package com.ronaldo.tripsuite.mapper;

import com.ronaldo.tripsuite.dto.TripDto;
import com.ronaldo.tripsuite.dto.UserDto;
import com.ronaldo.tripsuite.entity.Trip;
import com.ronaldo.tripsuite.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")

public interface TripMapper {

    TripMapper INSTANCE = Mappers.getMapper(TripMapper.class);

    TripDto tripToDto(Trip trip);

    List<TripDto> tripToDtoList(List<Trip> tripList);

    Trip dtoToTrip(TripDto tripDto);

    List<Trip> dtoToTripList(List<TripDto> tripDtoList);
}
