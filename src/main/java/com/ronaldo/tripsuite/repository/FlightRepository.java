package com.ronaldo.tripsuite.repository;

import com.ronaldo.tripsuite.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
}
