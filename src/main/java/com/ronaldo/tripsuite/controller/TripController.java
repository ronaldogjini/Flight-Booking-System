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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TripController {

    @Autowired
    private TripService tripService;
    
    @GetMapping({"/users/{userId}/trips"})
    public List<TripDto> filterTrips(@PathVariable Long userId, @RequestParam Optional<TripReason> reason, @RequestParam Optional<TripStatus> status) {
        return tripService.filterTrips(userId, reason, status);
    }

    @GetMapping({"/users/{userId}/trip/{tripId}"})
    public TripDto getTrip(@PathVariable Long userId, @PathVariable Long tripId) {
        return tripService.findById(userId, tripId);
    }

    @PostMapping({"/trips"})
    public TripDto saveTrip(@RequestBody TripDto tripDto) {
        return tripService.save(tripDto);
    }

    @PutMapping({"/trips"})
    public TripDto updateTripDetails(@RequestBody TripDto tripDto) {
        return tripService.updateDetails(tripDto);
    }


    // change status of the flight (CREATED -> WAITING_FOR_APPROVAL -> CONFIRMED/DELETED)
    @PatchMapping({"/trips/status"})
    public TripDto changeStatus(@RequestBody TripDto tripDto) {
        return tripService.changeStatus(tripDto);
    }


    @PostMapping({"/trips/flights"})
    public TripDto bookFlight(@RequestBody BookFlightRequestDto bookFlightRequestDto) {
        Long tripId = bookFlightRequestDto.getTripId();
        Long flightId = bookFlightRequestDto.getFlightId();

        return tripService.bookFlight(tripId, flightId);
    }
}
