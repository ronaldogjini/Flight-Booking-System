package com.ronaldo.tripsuite.controller;

import com.ronaldo.tripsuite.dto.BookFlightRequestDto;
import com.ronaldo.tripsuite.dto.TripDto;
import com.ronaldo.tripsuite.dto.TripStatusChangeDto;
import com.ronaldo.tripsuite.entity.FlightSchedule;
import com.ronaldo.tripsuite.entity.Trip;
import com.ronaldo.tripsuite.enums.TripReason;
import com.ronaldo.tripsuite.enums.TripStatus;
import com.ronaldo.tripsuite.mapper.TripMapper;
import com.ronaldo.tripsuite.service.TripService;
import com.ronaldo.tripsuite.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping({"/users/{userId}/trips"})
    @ApiOperation(value = "Retrieve trips of a user")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You do not have the proper permissions"),
            @ApiResponse(code = 401, message = "You are not authorized to do that")

    })
    public ResponseEntity<List<TripDto>> filterTrips(@PathVariable Long userId, @RequestParam Optional<TripReason> reason, @RequestParam Optional<TripStatus> status) {
        List<TripDto> filteredTrips = tripService.filterTrips(userId, reason, status);
        return ResponseEntity.ok().body(filteredTrips);
    }

    @GetMapping({"/users/{userId}/trip/{tripId}"})
    @ApiOperation(value = "Get a trip based on the user")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You do not have the proper permissions"),
            @ApiResponse(code = 401, message = "You are not authorized to do that")

    })
    public ResponseEntity<TripDto> getTrip(@PathVariable Long userId, @PathVariable Long tripId) {
        TripDto retrievedTrip = tripService.findById(userId, tripId);
        return ResponseEntity.ok().body(retrievedTrip);
    }

    @PostMapping({"/trips"})
    @ApiOperation(value = "Create a new trip")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You do not have the proper permissions"),
            @ApiResponse(code = 401, message = "You are not authorized to do that")

    })
    public ResponseEntity<TripDto> saveTrip(@Valid @RequestBody TripDto tripDto) {
        TripDto savedTrip = tripService.save(tripDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrip);

    }

    @DeleteMapping({"/trips/{id}"})
    @ApiOperation(value = "Delete a trip!")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You do not have the proper permissions"),
            @ApiResponse(code = 401, message = "You are not authorized to do that")

    })
    public ResponseEntity delete(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.ok().build();

    }

    @PutMapping({"/trips"})
    @ApiOperation(value = "Update trip details")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You do not have the proper permissions"),
            @ApiResponse(code = 401, message = "You are not authorized to do that")

    })
    public ResponseEntity<TripDto> updateTripDetails(@Valid @RequestBody TripDto tripDto) {
        TripDto updatedTrip = tripService.updateDetails(tripDto);
        return ResponseEntity.ok().body(updatedTrip);
    }


    // change status of the flight (CREATED -> WAITING_FOR_APPROVAL -> CONFIRMED/DELETED)
    @PatchMapping({"/trips/status"})
    @ApiOperation(value = "Change the trip status")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You do not have the proper permissions"),
            @ApiResponse(code = 401, message = "You are not authorized to do that")

    })
    public ResponseEntity<TripDto> changeStatus(@RequestBody TripDto tripDto) {
        TripDto updatedTrip = tripService.changeStatus(tripDto);
        return ResponseEntity.ok().body(updatedTrip);
    }


    @PostMapping({"/trips/flights"})
    @ApiOperation(value = "Add a flight to the trip")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You do not have the proper permissions"),
            @ApiResponse(code = 401, message = "You are not authorized to do that")

    })
    public ResponseEntity<TripDto> bookFlight(@Valid @RequestBody BookFlightRequestDto bookFlightRequestDto) {
        Long tripId = bookFlightRequestDto.getTripId();
        Long flightId = bookFlightRequestDto.getFlightId();

        TripDto tripWithFlights = tripService.bookFlight(tripId, flightId);
        return ResponseEntity.ok().body(tripWithFlights);

    }
}
