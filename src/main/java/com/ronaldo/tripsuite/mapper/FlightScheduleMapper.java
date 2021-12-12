package com.ronaldo.tripsuite.mapper;

import com.ronaldo.tripsuite.dto.FlightScheduleDto;
import com.ronaldo.tripsuite.dto.TripDto;
import com.ronaldo.tripsuite.entity.FlightSchedule;
import com.ronaldo.tripsuite.entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper(componentModel = "spring")
public interface FlightScheduleMapper {

    FlightScheduleMapper INSTANCE = Mappers.getMapper(FlightScheduleMapper.class);

    FlightScheduleDto flightScheduletoDto(FlightSchedule flightSchedule);
    List<FlightScheduleDto> flightScheduleToDtoList(List<FlightSchedule> flightScheduleList);

    FlightSchedule dtoToFlightSchedule(FlightScheduleDto flightScheduleDto);
    List<FlightSchedule> dtoToFlightScheduleList(List<FlightScheduleDto> flightScheduleDtoList);
}
