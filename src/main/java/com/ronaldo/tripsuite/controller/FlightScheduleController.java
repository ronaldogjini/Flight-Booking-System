package com.ronaldo.tripsuite.controller;

import com.ronaldo.tripsuite.dto.FlightScheduleDto;
import com.ronaldo.tripsuite.entity.FlightSchedule;
import com.ronaldo.tripsuite.service.FlightScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class FlightScheduleController {

    //TODO: Convert all FlightSchedule to FlightScheduleDTO

    @Autowired
    private FlightScheduleService flightScheduleService;

    @PostMapping({"/flightschedules"})
    public FlightScheduleDto insertFlightSchedule(@RequestBody FlightScheduleDto flightScheduleDto) {
        return flightScheduleService.saveFlightSchedule(flightScheduleDto);
    }

    @GetMapping({"/flightschedules"})
    public List<FlightSchedule> findAll() {
        return flightScheduleService.findAll();
    }

    @GetMapping({"/flightschedules/search"})
    public List<FlightSchedule> searchFlights(@RequestParam(required = false) String departureCity,
                                              @RequestParam(required = false) Date date) {
        return flightScheduleService.findFlights(departureCity, date);
    }
}
