package com.ronaldo.tripsuite.service.Impl;

import com.ronaldo.tripsuite.entity.Flight;
import com.ronaldo.tripsuite.entity.Trip;
import com.ronaldo.tripsuite.repository.FlightRepository;
import com.ronaldo.tripsuite.service.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Override
    public Flight findById(Long id) {
        Optional<Flight> flightOptional = flightRepository.findById(id);

        if (flightOptional.isEmpty()) {
            throw new NoSuchElementException();
        }

        log.info("New flight searched!");
        return flightOptional.get();
    }
}
