package com.ronaldo.tripsuite.controller;

import com.ronaldo.tripsuite.dto.FlightScheduleDto;
import com.ronaldo.tripsuite.entity.FlightSchedule;
import com.ronaldo.tripsuite.service.FlightScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class FlightScheduleController {

    @Autowired
    private FlightScheduleService flightScheduleService;

    @PreAuthorize("hasRole('Admin')")
    @PostMapping({"/flightschedules"})
    public ResponseEntity<FlightScheduleDto> saveFlightSchedule(@RequestBody FlightScheduleDto flightScheduleDto) {
        FlightScheduleDto savedFlightSchedule = flightScheduleService.saveFlightSchedule(flightScheduleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFlightSchedule);
    }

    @GetMapping({"/flightschedules"})
    public ResponseEntity<List<FlightScheduleDto>> searchFlights(@RequestParam Optional<String> departureCity,
                                                                 @RequestParam Optional<String> arrivalCity,
                                                                 @RequestParam Optional<Date> departureDate) {

        List<FlightScheduleDto> foundFlights = flightScheduleService.findFlights(departureCity, arrivalCity, departureDate);
        return ResponseEntity.ok().body(foundFlights);
    }
}
