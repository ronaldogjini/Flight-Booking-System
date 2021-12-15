package com.ronaldo.tripsuite.service.Impl;

import com.ronaldo.tripsuite.dto.FlightScheduleDto;
import com.ronaldo.tripsuite.entity.Flight;
import com.ronaldo.tripsuite.entity.FlightSchedule;
import com.ronaldo.tripsuite.entity.Plane;
import com.ronaldo.tripsuite.entity.Trip;
import com.ronaldo.tripsuite.mapper.FlightScheduleMapper;
import com.ronaldo.tripsuite.repository.FlightScheduleRepository;
import com.ronaldo.tripsuite.repository.PlaneRepository;
import com.ronaldo.tripsuite.service.FlightScheduleService;
import com.ronaldo.tripsuite.service.FlightService;
import com.ronaldo.tripsuite.service.PlaneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class FlightScheduleServiceImpl implements FlightScheduleService {

    private final FlightScheduleRepository flightScheduleRepository;
    private final PlaneService planeService;
    private final FlightService flightService;
    private final FlightScheduleMapper flightScheduleMapper;

    public FlightScheduleServiceImpl(FlightScheduleRepository flightScheduleRepository, PlaneRepository planeRepository, PlaneService planeService, FlightService flightService, FlightScheduleMapper flightScheduleMapper) {
        this.flightScheduleRepository = flightScheduleRepository;
        this.planeService = planeService;
        this.flightService = flightService;
        this.flightScheduleMapper = flightScheduleMapper;
    }

    @Override
    public List<FlightSchedule> findAll() {
        return flightScheduleRepository.findAll();
    }

    @Override
    public List<FlightScheduleDto> searchFlightSchedules(Optional<String> departureCity, Optional<String> arrivalCity, Optional<Date> date) {

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

        Plane foundPlane = planeService.findById(flightScheduleDto.getPlaneId());
        Flight foundFlight = flightService.findById(flightScheduleDto.getFlightId());

        FlightSchedule newFlightSchedule = flightScheduleMapper.dtoToFlightSchedule(flightScheduleDto);

        Date currentDate = new Date(System.currentTimeMillis());
        Date arrivalDate = flightScheduleDto.getArrivalDate();
        Date departureDate = flightScheduleDto.getDepartureDate();

        if (arrivalDate.before(currentDate) ||
                departureDate.before(currentDate) ||
                arrivalDate.before(departureDate)) {
            throw new IllegalArgumentException("Dates are wrong!");
        }

        newFlightSchedule.setPlane(foundPlane);
        newFlightSchedule.setFlight(foundFlight);

        log.info("New flight schedule added!");

        FlightSchedule savedFlightSchedule = flightScheduleRepository.save(newFlightSchedule);
        FlightScheduleDto savedFlightScheduleDto = flightScheduleMapper.flightScheduletoDto(savedFlightSchedule);
        savedFlightScheduleDto.setFlightId(savedFlightSchedule.getFlight().getId());
        savedFlightScheduleDto.setPlaneId(savedFlightSchedule.getPlane().getId());
        return savedFlightScheduleDto;
    }

    public FlightSchedule getById(Long id) {
        Optional<FlightSchedule> flightSchedule = flightScheduleRepository.findById(id);

        if (flightSchedule.isEmpty()) {
            throw new NoSuchElementException();
        }

        return flightSchedule.get();
    }
}
