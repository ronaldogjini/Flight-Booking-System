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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping({"/users/{userId}/trips"})
    public ResponseEntity<List<TripDto>> filterTrips(@PathVariable Long userId, @RequestParam Optional<TripReason> reason, @RequestParam Optional<TripStatus> status) {
        List<TripDto> filteredTrips = tripService.filterTrips(userId, reason, status);
        return ResponseEntity.ok().body(filteredTrips);
    }

    @GetMapping({"/users/{userId}/trip/{tripId}"})
    public ResponseEntity<TripDto> getTrip(@PathVariable Long userId, @PathVariable Long tripId) {
        TripDto retrievedTrip = tripService.findById(userId, tripId);
        return ResponseEntity.ok().body(retrievedTrip);
    }

    @PostMapping({"/trips"})
    public ResponseEntity<TripDto> saveTrip(@RequestBody TripDto tripDto) {
        TripDto savedTrip = tripService.save(tripDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrip);

    }

    @PutMapping({"/trips"})
    public ResponseEntity<TripDto> updateTripDetails(@RequestBody TripDto tripDto) {
        TripDto updatedTrip = tripService.updateDetails(tripDto);
        return ResponseEntity.ok().body(updatedTrip);
    }


    // change status of the flight (CREATED -> WAITING_FOR_APPROVAL -> CONFIRMED/DELETED)
    @PatchMapping({"/trips/status"})
    public ResponseEntity<TripDto> changeStatus(@RequestBody TripDto tripDto) {
        TripDto updatedTrip = tripService.changeStatus(tripDto);
        return ResponseEntity.ok().body(updatedTrip);
    }


    @PostMapping({"/trips/flights"})
    public ResponseEntity<TripDto> bookFlight(@RequestBody BookFlightRequestDto bookFlightRequestDto) {
        Long tripId = bookFlightRequestDto.getTripId();
        Long flightId = bookFlightRequestDto.getFlightId();

        TripDto tripWithFlights = tripService.bookFlight(tripId, flightId);
        return ResponseEntity.ok().body(tripWithFlights);

    }
}
