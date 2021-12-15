package com.ronaldo.tripsuite.service.Impl;

import com.ronaldo.tripsuite.dto.TripDto;
import com.ronaldo.tripsuite.entity.Flight;
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
import lombok.extern.slf4j.Slf4j;
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

    private final TripRepository tripRepository;
    private final FlightScheduleService flightScheduleService;
    private final TripMapper tripMapper;
    private final JwtUtil jwtUtil;

    public TripServiceImpl(TripRepository tripRepository, FlightScheduleRepository flightScheduleService, FlightScheduleService flightScheduleService1, TripMapper tripMapper, JwtUtil jwtUtil) {
        this.tripRepository = tripRepository;
        this.flightScheduleService = flightScheduleService1;
        this.tripMapper = tripMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public TripDto save(TripDto NewTripDto) {

        Trip newTrip = tripMapper.dtoToTrip(NewTripDto);
        List<Trip> allTrips = findAll();

        allTrips.forEach(trip -> {
            if (trip.getDepartureDate().before(newTrip.getDepartureDate())
                    && trip.getArrivalDate().after(newTrip.getDepartureDate())) {
                throw new IllegalArgumentException("A trip already exists during these dates!");
            }
        });

        Date currentDate = new Date(System.currentTimeMillis());
        Date departureDate = newTrip.getDepartureDate();
        Date arrivalDate = newTrip.getArrivalDate();

        if (arrivalDate.before(currentDate) ||
                departureDate.before(currentDate) ||
                arrivalDate.before(departureDate)) {
            throw new IllegalArgumentException("Dates are not correct!");
        }

        newTrip.setStatus(TripStatus.CREATED);
        newTrip.setUserId(jwtUtil.getLoggedInUser().getId());

        Trip savedTrip = tripRepository.save(newTrip);

        log.info("New trip saved!");
        return tripMapper.tripToDto(savedTrip);
    }

    @Override
    public TripDto changeStatus(TripDto tripWithNewStatus) {
        Trip existingTrip = getById(tripWithNewStatus.getId());
        Trip updatedTrip = null;

        if (tripWithNewStatus.getStatus() == null) {
            throw new IllegalArgumentException("Please add a status");
        }

        if (!existingTrip.getUserId().equals(jwtUtil.getLoggedInUser().getId())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }

        TripStatus newStatus = tripWithNewStatus.getStatus();

        switch (newStatus) {
            case CREATED: {
                throw new IllegalArgumentException("Trip cannot go back to CREATED");
            }
            case WAITING_FOR_APPROVAL: {
                sendTripForAdminApproval(existingTrip, newStatus);
                updatedTrip = tripRepository.save(existingTrip);
                break;
            }
            case APPROVED: {
                approveTrip(existingTrip, newStatus);
                updatedTrip = tripRepository.save(existingTrip);
                break;
            }
            case REJECTED: {
                rejectTrip(existingTrip, newStatus);
                updatedTrip = tripRepository.save(existingTrip);
                break;

            }
        }

        log.info("Trip newStatus changed!");
        return tripMapper.tripToDto(updatedTrip);
    }

    @Override
    public TripDto updateDetails(TripDto updatedTripDto) {
        Trip existingTrip = getById(updatedTripDto.getId());

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
        existingTrip.setReason(updatedTripDto.getReason());
        existingTrip.setDescription(updatedTripDto.getDescription());

        log.info("Trip details updated!");

        return tripMapper.tripToDto(tripRepository.save(existingTrip));
    }

    @Override
    public List<TripDto> findUserTrips(Long userId, Optional<TripReason> reason, Optional<TripStatus> status) {

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

    @Override
    public TripDto findUserTripById(Long userId, Long tripId) {
        Trip existingTrip = getById(tripId);

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
    public void softDelete(Long id) {

        Trip existingTrip = getById(id);

        if (!existingTrip.getUserId().equals(jwtUtil.getLoggedInUser().getId())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }

        log.info("Trip soft deleted!");
        tripRepository.softDelete(id);
    }

    @Override
    public TripDto bookFlight(Long tripId, Long flightId) {
        Trip existingTrip = getById(tripId);
        FlightSchedule existingFlight = flightScheduleService.getById(flightId);

        if (!existingTrip.getUserId().equals(jwtUtil.getLoggedInUser().getId())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }

        if (existingTrip.getStatus() != TripStatus.APPROVED) {
            throw new UnsupportedOperationException("The trip has not been approved!");
        }

        if (existingTrip.getFlightSchedules().contains(existingFlight)) {
            throw new UnsupportedOperationException("This flight is already added!");
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

    private Trip getById(Long id) {
        Optional<Trip> trip = tripRepository.findById(id);

        if (trip.isEmpty()) {
            throw new NoSuchElementException();
        }

        return trip.get();
    }

    private void sendTripForAdminApproval(Trip existingTrip, TripStatus status) {
        if (existingTrip.getStatus() == TripStatus.WAITING_FOR_APPROVAL || existingTrip.getStatus() == TripStatus.APPROVED) {
            throw new UnsupportedOperationException("You can no longer ask for approval!");
        }
        existingTrip.setStatus(status);
    }

    private void approveTrip(Trip existingTrip, TripStatus status) {
        if (existingTrip.getStatus() == TripStatus.CREATED || existingTrip.getStatus() == TripStatus.APPROVED) {
            throw new UnsupportedOperationException("You cannot approve at this stage!");
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
