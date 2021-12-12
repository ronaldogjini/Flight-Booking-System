package com.ronaldo.tripsuite.service.Impl;

import com.ronaldo.tripsuite.dto.FlightScheduleDto;
import com.ronaldo.tripsuite.entity.FlightSchedule;
import com.ronaldo.tripsuite.entity.Plane;
import com.ronaldo.tripsuite.mapper.FlightScheduleMapper;
import com.ronaldo.tripsuite.repository.FlightScheduleRepository;
import com.ronaldo.tripsuite.repository.PlaneRepository;
import com.ronaldo.tripsuite.service.FlightScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
@Service
public class FlightScheduleServiceImpl implements FlightScheduleService {

    @Autowired
    private FlightScheduleRepository flightScheduleRepository;

    @Autowired
    private PlaneRepository planeRepository;

    @Autowired
    private FlightScheduleMapper flightScheduleMapper;

    @Override
    public List<FlightSchedule> findAll() {
        return flightScheduleRepository.findAll();
    }

    @Override
    public List<FlightSchedule> findFlights(String departureCity, Date date) {

        if(departureCity != null & date != null) {
            return flightScheduleRepository.findByDepartureCityAndDate(departureCity, date);
        } else if (departureCity != null) {
            Date currentDate = new Date(System.currentTimeMillis());
            return flightScheduleRepository.findByDepartureCityAndDate(departureCity, currentDate);
        }

        return flightScheduleRepository.findAll();

    }

    @Override
    public FlightScheduleDto saveFlightSchedule(FlightScheduleDto flightScheduleDto) {

        FlightSchedule newFlightSchedule = flightScheduleMapper.dtoToFlightSchedule(flightScheduleDto);
        String arrivalTime = newFlightSchedule.getArrivalTime().toString();
        String departureTime = newFlightSchedule.getArrivalTime().toString();

        newFlightSchedule.setDepartureTime(Time.valueOf(departureTime));
        newFlightSchedule.setArrivalTime(Time.valueOf(arrivalTime));

        Plane plane = planeRepository.findById("B737").get();
        newFlightSchedule.setPlane(plane);

        return flightScheduleMapper
                .flightScheduletoDto(flightScheduleRepository
                        .save(newFlightSchedule));
    }
}
