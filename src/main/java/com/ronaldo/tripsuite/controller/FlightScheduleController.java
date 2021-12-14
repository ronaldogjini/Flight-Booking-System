package com.ronaldo.tripsuite.controller;

import com.ronaldo.tripsuite.dto.FlightScheduleDto;
import com.ronaldo.tripsuite.entity.FlightSchedule;
import com.ronaldo.tripsuite.service.FlightScheduleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@RequestMapping("/api")
public class FlightScheduleController {

    @Autowired
    private FlightScheduleService flightScheduleService;

    @PreAuthorize("hasRole('Admin')")
    @PostMapping({"/flightschedules"})
    @ApiOperation(value = "Add a new flight schedule based on an existing flight plan")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You do not have the proper permissions"),
            @ApiResponse(code = 401, message = "You are not authorized to do that")

    })
    public ResponseEntity<FlightScheduleDto> saveFlightSchedule(@RequestBody FlightScheduleDto flightScheduleDto) {
        FlightScheduleDto savedFlightSchedule = flightScheduleService.saveFlightSchedule(flightScheduleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFlightSchedule);
    }

    @GetMapping({"/flightschedules"})
    @ApiOperation(value = "Search for flights based on date and location")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You do not have the proper permissions"),
            @ApiResponse(code = 401, message = "You are not authorized to do that")

    })
    public ResponseEntity<List<FlightScheduleDto>> searchFlights(@RequestParam Optional<String> departureCity,
                                                                 @RequestParam Optional<String> arrivalCity,
                                                                 @RequestParam Optional<Date> departureDate) {

        List<FlightScheduleDto> foundFlights = flightScheduleService.findFlights(departureCity, arrivalCity, departureDate);
        return ResponseEntity.ok().body(foundFlights);
    }
}
