package com.ronaldo.tripsuite.service.Impl;

import com.ronaldo.tripsuite.dto.FlightScheduleDto;
import com.ronaldo.tripsuite.entity.Flight;
import com.ronaldo.tripsuite.entity.FlightSchedule;
import com.ronaldo.tripsuite.entity.Plane;
import com.ronaldo.tripsuite.mapper.FlightScheduleMapper;
import com.ronaldo.tripsuite.repository.FlightScheduleRepository;
import com.ronaldo.tripsuite.repository.PlaneRepository;
import com.ronaldo.tripsuite.service.FlightScheduleService;
import com.ronaldo.tripsuite.service.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FlightScheduleServiceImpl implements FlightScheduleService {

    @Autowired
    private FlightScheduleRepository flightScheduleRepository;

    @Autowired
    private PlaneRepository planeRepository;

    @Autowired
    private FlightService flightService;

    @Autowired
    private FlightScheduleMapper flightScheduleMapper;

    @Override
    public List<FlightSchedule> findAll() {
        return flightScheduleRepository.findAll();
    }

    @Override
    public List<FlightScheduleDto> findFlights(Optional<String> departureCity, Optional<String> arrivalCity, Optional<Date> date) {

        Date currentDate = new Date(System.currentTimeMillis());
        List<FlightSchedule> foundFlights;

        if (date.isPresent() && date.get().before(currentDate)) {
            throw new IllegalArgumentException("The date cannot be in the past!");
        }

        if (departureCity.isPresent() && arrivalCity.isPresent() && date.isPresent()) {
            foundFlights = flightScheduleRepository
                    .findByDepartureAndArrivalCityAndDate(departureCity.get(), arrivalCity.get(), date.get());
        } else if (departureCity.isPresent() && arrivalCity.isPresent()) {
            foundFlights = flightScheduleRepository
                    .findByDepartureAndArrivalCityAndDate(departureCity.get(), arrivalCity.get(), currentDate);
        } else if (departureCity.isPresent() && date.isPresent()) {
            foundFlights = flightScheduleRepository
                    .findByDepartureCityAndDate(departureCity.get(), date.get());
        } else if (departureCity.isPresent()) {
            foundFlights = flightScheduleRepository
                    .findByDepartureCityAndDate(departureCity.get(), currentDate);
        } else {
            foundFlights = flightScheduleRepository.findFlightsAfterCurrentDate(currentDate);
        }

        log.info("Flight schedules filtered!");
        return flightScheduleMapper.flightScheduleToDtoList(foundFlights);
    }

    @Override
    public FlightScheduleDto saveFlightSchedule(FlightScheduleDto flightScheduleDto) {

        Plane foundPlane = planeRepository.findById("B737").get();
        Flight foundFlight = flightService.findById(flightScheduleDto.getFlightId());

        FlightSchedule newFlightSchedule = flightScheduleMapper.dtoToFlightSchedule(flightScheduleDto);

        newFlightSchedule.setPlane(foundPlane);
        newFlightSchedule.setFlight(foundFlight);

        log.info("New flight schedule added!");

        return flightScheduleMapper
                .flightScheduletoDto(flightScheduleRepository
                        .save(newFlightSchedule));
    }
}
