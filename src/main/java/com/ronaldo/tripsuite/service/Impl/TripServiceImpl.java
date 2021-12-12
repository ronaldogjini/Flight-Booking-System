package com.ronaldo.tripsuite.service.Impl;

import com.ronaldo.tripsuite.dto.TripDto;
import com.ronaldo.tripsuite.entity.FlightSchedule;
import com.ronaldo.tripsuite.entity.Trip;
import com.ronaldo.tripsuite.enums.TripReason;
import com.ronaldo.tripsuite.enums.TripStatus;
import com.ronaldo.tripsuite.mapper.TripMapper;
import com.ronaldo.tripsuite.repository.FlightScheduleRepository;
import com.ronaldo.tripsuite.repository.TripRepository;
import com.ronaldo.tripsuite.service.FlightScheduleService;
import com.ronaldo.tripsuite.service.TripService;
import com.ronaldo.tripsuite.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.client.HttpClientErrorException;

import javax.swing.text.html.Option;
import java.nio.file.AccessDeniedException;
import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private FlightScheduleRepository flightScheduleRepository;

    @Autowired
    private TripMapper tripMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public TripDto findById(Long userId, Long tripId) {
        Optional<Trip> trip = tripRepository.findById(tripId);

        if (trip.isEmpty()) {
            throw new NoSuchElementException();
        }

        Trip existingTrip = trip.get();

        if (!userId.equals(jwtUtil.getLoggedInUser().getId()) ||
                !existingTrip.getUserId().equals(jwtUtil.getLoggedInUser().getId())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }

        return tripMapper.tripToDto(existingTrip);
    }

    @Override
    public List<Trip> findAll() {
        return tripRepository.findAll();
    }

    @Override
    public TripDto save(TripDto tripDto) {

        Trip newTrip = tripMapper.dtoToTrip(tripDto);

        Date arrivalDate = newTrip.getArrivalDate();
        Date departureDate = newTrip.getDepartureDate();

        if (arrivalDate.before(departureDate)) {
            throw new IllegalArgumentException();
        }

        newTrip.setStatus(TripStatus.CREATED);
        newTrip.setUserId(jwtUtil.getLoggedInUser().getId());

        Trip savedTrip = tripRepository.save(newTrip);
        return tripMapper.tripToDto(savedTrip);
    }

    @Override
    public TripDto changeStatus(TripDto tripWithNewStatus) {
        Optional<Trip> trip = tripRepository.findById(tripWithNewStatus.getId());
        Trip updatedTrip = null;

        if (trip.isEmpty()) {
            throw new NoSuchElementException();
        }

        Trip existingTrip = trip.get();

        if (!existingTrip.getUserId().equals(jwtUtil.getLoggedInUser().getId())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }

        TripStatus status = tripWithNewStatus.getStatus();

        switch (status) {
            case CREATED: {
                throw new IllegalArgumentException();

            }
            case WAITING_FOR_APPROVAL: {
                sendTripForAdminApproval(existingTrip, status);
                updatedTrip = tripRepository.save(existingTrip);
                break;
            }
            case APPROVED: {
                approveTrip(existingTrip, status);
                updatedTrip = tripRepository.save(existingTrip);
                break;
            }
            case REJECTED: {
                rejectTrip(existingTrip, status);
                updatedTrip = tripRepository.save(existingTrip);
                break;
            }
        }
        return tripMapper.tripToDto(updatedTrip);
    }

    @Override
    public TripDto updateDetails(TripDto updatedTripDto) {
        Optional<Trip> existingTripOptional = tripRepository.findById(updatedTripDto.getId());
        Trip existingTrip;

        if (existingTripOptional.isEmpty()) {
            throw new NoSuchElementException();
        }

        existingTrip = existingTripOptional.get();

        if (!existingTrip.getUserId().equals(jwtUtil.getLoggedInUser().getId())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }

        if (existingTrip.getStatus() != TripStatus.CREATED) {
            throw new IllegalArgumentException();
        }

        existingTrip.setArrivalDate(updatedTripDto.getArrivalDate());
        existingTrip.setDepartureDate(updatedTripDto.getDepartureDate());
        existingTrip.setArrivalLocation(updatedTripDto.getArrivalLocation());
        existingTrip.setDepartureLocation(updatedTripDto.getDepartureLocation());

        return tripMapper.tripToDto(tripRepository.save(existingTrip));
    }

    @Override
    public TripDto bookFlight(Long tripId, Long flightId) {
        Optional<Trip> existingTripOpt = tripRepository.findById(tripId);
        Optional<FlightSchedule> existingFlightOpt = flightScheduleRepository.findById(flightId);

        if (existingTripOpt.isEmpty()) {
            throw new NoSuchElementException();
        }

        Trip existingTrip = existingTripOpt.get();

        if (!existingTrip.getUserId().equals(jwtUtil.getLoggedInUser().getId())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }

        if (existingFlightOpt.isEmpty()) {
            throw new NoSuchElementException();
        }

        FlightSchedule existingFlight = existingFlightOpt.get();

        if (existingFlight.getDepartureDate().before(existingFlight.getDepartureDate()) ||
                existingFlight.getArrivalDate().after(existingFlight.getArrivalDate())) {
            throw new IllegalArgumentException();
        }

        existingTrip.getFlightSchedules().add(existingFlight);
        Trip savedTrip = tripRepository.save(existingTrip);

        return tripMapper.tripToDto(savedTrip);
    }

    @Override
    public List<TripDto> filterTrips(Long userId, Optional<TripReason> reason, Optional<TripStatus> status) {

        List<Trip> filteredTrips;
        Long loggedUserId = jwtUtil.getLoggedInUser().getId();

        if (!userId.equals(loggedUserId)) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }

        if (reason.isPresent() && status.isPresent()) {
            filteredTrips = tripRepository.findByUserIdAndReasonAndStatus(userId, reason.get(), status.get());
        } else if (reason.isPresent()) {
            filteredTrips = tripRepository.findByUserIdAndReason(userId, reason.get());
        } else if (status.isPresent()) {
            filteredTrips = tripRepository.findByUserIdAndStatus(userId, status.get());
        } else {
            filteredTrips = tripRepository.findByUserId(userId);
        }

        return tripMapper.tripToDtoList(filteredTrips);
    }

    private void sendTripForAdminApproval(Trip existingTrip, TripStatus status) {
        if (existingTrip.getStatus() == TripStatus.WAITING_FOR_APPROVAL && existingTrip.getStatus() == TripStatus.APPROVED) {
            throw new IllegalArgumentException();
        } else {
            existingTrip.setStatus(status);
        }
    }

    private void approveTrip(Trip existingTrip, TripStatus status) {
        if (existingTrip.getStatus() == TripStatus.CREATED && existingTrip.getStatus() == TripStatus.APPROVED) {
            throw new IllegalArgumentException();
        }

        if (!jwtUtil.loggedUserIsAdmin()) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }

        existingTrip.setStatus(status);

    }

    private void rejectTrip(Trip existingTrip, TripStatus status) {

        if (!jwtUtil.loggedUserIsAdmin()) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);

        }

        if (existingTrip.getStatus() != TripStatus.WAITING_FOR_APPROVAL) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        existingTrip.setStatus(status);
    }

}
