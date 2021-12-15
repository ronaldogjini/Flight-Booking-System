package com.ronaldo.tripsuite.service.Impl;

import com.ronaldo.tripsuite.dto.TripDto;
import com.ronaldo.tripsuite.entity.FlightSchedule;
import com.ronaldo.tripsuite.entity.Trip;
import com.ronaldo.tripsuite.enums.TripReason;
import com.ronaldo.tripsuite.enums.TripStatus;
import com.ronaldo.tripsuite.mapper.TripMapper;
import com.ronaldo.tripsuite.repository.FlightScheduleRepository;
import com.ronaldo.tripsuite.repository.TripRepository;
import com.ronaldo.tripsuite.service.TripService;
import com.ronaldo.tripsuite.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
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

        log.info("Trip fetched by ID");
        return tripMapper.tripToDto(existingTrip);
    }

    @Override
    public List<Trip> findAll() {

        log.info("All trips fetched!");
        return tripRepository.findAll();
    }

    @Override
    public TripDto save(TripDto tripDto) {

        Trip newTrip = tripMapper.dtoToTrip(tripDto);

        List<Trip> allTrips = findAll();

        allTrips.forEach(trip -> {
            if (trip.getDepartureDate().before(newTrip.getDepartureDate())
                    && trip.getArrivalDate().after(newTrip.getDepartureDate())) {
                throw new IllegalArgumentException("A trip already exists during these dates!");
            }
        });

        Date currentDate = new Date(System.currentTimeMillis());
        Date arrivalDate = newTrip.getArrivalDate();
        Date departureDate = newTrip.getDepartureDate();

        if (arrivalDate.before(currentDate) ||
                departureDate.before(currentDate) ||
                departureDate.before(arrivalDate)) {
            throw new IllegalArgumentException("Dates are not correct!");
        }

        newTrip.setStatus(TripStatus.CREATED);
        newTrip.setUserId(jwtUtil.getLoggedInUser().getId());

        Trip savedTrip = tripRepository.save(newTrip);

        log.info("New trip saved!");
        return tripMapper.tripToDto(savedTrip);
    }

    @Override
    public void deleteTrip(Long id) {

        log.info("Trip soft deleted!");
        tripRepository.softDelete(id);
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
                throw new IllegalArgumentException("Trip cannot go back to CREATED");
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
        log.info("Trip status changed!");
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
            throw new IllegalArgumentException("The trip cannot be updated at this stage!");
        }

        Date currentDate = new Date(System.currentTimeMillis());
        Date arrivalDate = updatedTripDto.getArrivalDate();
        Date departureDate = updatedTripDto.getDepartureDate();
        String departureLocation = updatedTripDto.getDepartureLocation();
        String arrivalLocation = updatedTripDto.getArrivalLocation();

        if (arrivalDate.before(currentDate) ||
                departureDate.before(currentDate) ||
                arrivalDate.before(departureDate)) {
            throw new IllegalArgumentException("Dates are wrong!");
        }

        existingTrip.setArrivalDate(arrivalDate);
        existingTrip.setDepartureDate(departureDate);
        existingTrip.setArrivalLocation(arrivalLocation);
        existingTrip.setDepartureLocation(departureLocation);

        log.info("Trip details updated!");

        return tripMapper.tripToDto(tripRepository.save(existingTrip));
    }

    @Override
    public TripDto bookFlight(Long tripId, Long flightId) {
        Optional<Trip> existingTripOpt = tripRepository.findById(tripId);
        Optional<FlightSchedule> existingFlightOpt = flightScheduleRepository.findById(flightId);


        if (existingTripOpt.isEmpty()) {
            throw new NoSuchElementException("There is no existing trip with this ID");
        }

        Trip existingTrip = existingTripOpt.get();

        if (!existingTrip.getUserId().equals(jwtUtil.getLoggedInUser().getId())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }

        if (existingFlightOpt.isEmpty()) {
            throw new NoSuchElementException("There are no existing flights with this ID");
        }

        FlightSchedule existingFlight = existingFlightOpt.get();

        if (existingTrip.getFlightSchedules().contains(existingFlight)) {
            throw new IllegalArgumentException("This flight is already added!");
        }

        if (existingFlight.getDepartureDate().before(existingFlight.getDepartureDate()) ||
                existingFlight.getArrivalDate().after(existingFlight.getArrivalDate())) {
            throw new IllegalArgumentException("Dates are wrong!");
        }

        existingTrip.getFlightSchedules().add(existingFlight);
        Trip savedTrip = tripRepository.save(existingTrip);

        log.info("Flight added to trip!");
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

        log.info("Trips filtered!");
        return tripMapper.tripToDtoList(filteredTrips);
    }

    private void sendTripForAdminApproval(Trip existingTrip, TripStatus status) {
        if (existingTrip.getStatus() == TripStatus.WAITING_FOR_APPROVAL && existingTrip.getStatus() == TripStatus.APPROVED) {
            throw new IllegalArgumentException("You can no longer ask for approval!");
        } else {
            existingTrip.setStatus(status);
        }
    }

    private void approveTrip(Trip existingTrip, TripStatus status) {
        if (existingTrip.getStatus() == TripStatus.CREATED && existingTrip.getStatus() == TripStatus.APPROVED) {
            throw new IllegalArgumentException("You cannot approve at this stage!");
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
