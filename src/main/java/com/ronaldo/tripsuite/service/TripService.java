package com.ronaldo.tripsuite.service;

import com.ronaldo.tripsuite.dto.TripDto;
import com.ronaldo.tripsuite.entity.Trip;
import com.ronaldo.tripsuite.enums.TripReason;
import com.ronaldo.tripsuite.enums.TripStatus;

import java.util.List;
import java.util.Optional;

public interface TripService {

    TripDto findUserTripById(Long userId, Long tripId);

    List<Trip> findAll();

    TripDto save(TripDto trip);

    void softDelete(Long id);

    TripDto changeStatus(TripDto trip);

    TripDto updateDetails(TripDto trip);

    TripDto bookFlight(Long tripId, Long flightId);

    List<TripDto> findUserTrips(Long id, Optional<TripReason> reason, Optional<TripStatus> status);
}
