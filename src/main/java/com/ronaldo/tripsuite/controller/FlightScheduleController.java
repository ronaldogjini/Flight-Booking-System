package com.ronaldo.tripsuite.controller;

import com.ronaldo.tripsuite.dto.FlightScheduleDto;
import com.ronaldo.tripsuite.service.FlightScheduleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FlightScheduleController {

    private final FlightScheduleService flightScheduleService;

    public FlightScheduleController(FlightScheduleService flightScheduleService) {
        this.flightScheduleService = flightScheduleService;
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping({"/flightschedules"})
    @ApiOperation(value = "Add a new flight schedule based on an existing flight plan")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You do not have the proper permissions"),
            @ApiResponse(code = 401, message = "You are not authorized to do that")

    })
    public ResponseEntity<FlightScheduleDto> saveFlightSchedule(@Valid @RequestBody FlightScheduleDto flightScheduleDto) {
        FlightScheduleDto savedFlightSchedule = flightScheduleService.saveFlightSchedule(flightScheduleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFlightSchedule);
    }

    @GetMapping({"/flightschedules"})
    @ApiOperation(value = "Search for flights based on date and location")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You do not have the proper permissions"),
            @ApiResponse(code = 401, message = "You are not authorized to do that")

    })
    public ResponseEntity<List<FlightScheduleDto>> searchFlightSchedules(@RequestParam Optional<String> departureCity,
                                                                         @RequestParam Optional<String> arrivalCity,
                                                                         @RequestParam Optional<Date> departureDate) {

        List<FlightScheduleDto> foundFlights = flightScheduleService.searchFlightSchedules(departureCity, arrivalCity, departureDate);
        return ResponseEntity.ok().body(foundFlights);
    }
}
