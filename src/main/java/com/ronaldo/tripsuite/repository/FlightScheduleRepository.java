package com.ronaldo.tripsuite.repository;

import com.ronaldo.tripsuite.entity.FlightSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface FlightScheduleRepository extends JpaRepository<FlightSchedule, Long> {

    @Query("SELECT f from FlightSchedule f INNER JOIN f.flight fl JOIN fl.departureAirport departure JOIN fl.destinationAirport destination where departure.city like ?1% AND destination.city like ?2% AND f.departureDate >= ?3")
    List<FlightSchedule> findByDepartureAndArrivalCityAndDate(String departureCity, String arrivalCity, Date departureDate);

    @Query("SELECT f from FlightSchedule f INNER JOIN f.flight fl JOIN fl.departureAirport airport  where airport.city like ?1% AND f.departureDate >= ?2")
    List<FlightSchedule> findByDepartureCityAndDate(String departureCity, Date departureDate);

    @Query("SELECT f from FlightSchedule f where  f.departureDate >= ?1")
    List<FlightSchedule> findFlightsAfterCurrentDate(Date currentDate);

}
