package com.ronaldo.tripsuite.service;

import com.ronaldo.tripsuite.entity.Flight;
import com.ronaldo.tripsuite.entity.Trip;

public interface FlightService {


    Flight findById(Long id);
}
