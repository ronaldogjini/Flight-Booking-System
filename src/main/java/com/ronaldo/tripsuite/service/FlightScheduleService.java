package com.ronaldo.tripsuite.service;

import com.ronaldo.tripsuite.dto.FlightScheduleDto;
import com.ronaldo.tripsuite.entity.Flight;
import com.ronaldo.tripsuite.entity.FlightSchedule;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface FlightScheduleService {

    List<FlightSchedule> findAll();

    List<FlightSchedule> findFlights(String departureCity, Date date);

    FlightScheduleDto saveFlightSchedule(FlightScheduleDto flightSchedule);
}
