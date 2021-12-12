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
import org.springframework.web.client.HttpClientErrorException;

import javax.swing.text.html.Option;
import java.sql.Date;
import java.util.List;
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

        if (!trip.isPresent()) {
            System.out.println("Not found");
            return null;
        }

        Trip existingTrip = trip.get();

        if (!userId.equals(jwtUtil.getLoggedInUser().getId()) ||
                !existingTrip.getUserId().equals(jwtUtil.getLoggedInUser().getId())) {
            System.out.println("Not authorized!");
            return null;
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
            System.out.println("Arrival date cannot be before departure date");
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

        if (!trip.isPresent()) {
            System.out.println("Trip Not Found");
            return null;
        }

        Trip existingTrip = trip.get();

        if (!existingTrip.getUserId().equals(jwtUtil.getLoggedInUser().getId())) {
            System.out.println("You are not authorized!");
        }

        TripStatus status = tripWithNewStatus.getStatus();

        switch (status) {
            case CREATED: {
                System.out.println("Cannot change status to Created");
                return null;

            }
            case WAITING_FOR_APPROVAL: {
                existingTrip = sendTripForAdminApproval(existingTrip, status);
                assert existingTrip != null;
                updatedTrip = tripRepository.save(existingTrip);
                break;
            }
            case APPROVED: {
                existingTrip = approveTrip(existingTrip, status);
                assert existingTrip != null;
                updatedTrip = tripRepository.save(existingTrip);
                break;
            }
            case REJECTED: {
                existingTrip = rejectTrip(existingTrip, status);
                assert existingTrip != null;
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

        if (!existingTripOptional.isPresent()) {
            System.out.println("Trip Not Found");
            return null;
        }

        existingTrip = existingTripOptional.get();

        if (!existingTrip.getUserId().equals(jwtUtil.getLoggedInUser().getId())) {
            System.out.println("Not authorized!");
            return null;
        }

        if (existingTrip.getStatus() != TripStatus.CREATED) {
            System.out.println("You can no longer update this trip!");
            return null;
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

        if (!existingTripOpt.isPresent()) {
            System.out.println("Trip does not exist");
            return null;
        }

        Trip existingTrip = existingTripOpt.get();

        if (!existingTrip.getUserId().equals(jwtUtil.getLoggedInUser().getId())) {
            System.out.println("Not authorized");
            return null;
        }

        if (!existingFlightOpt.isPresent()) {
            System.out.println("Flight does not exist");
            return null;
        }

        FlightSchedule existingFlight = existingFlightOpt.get();

        existingTrip.getFlightSchedules().add(existingFlight);
        Trip savedTrip = tripRepository.save(existingTrip);

        return tripMapper.tripToDto(savedTrip);
    }

    @Override
    public List<TripDto> filterTrips(Long userId, Optional<TripReason> reason, Optional<TripStatus> status) {

        List<Trip> filteredTrips;
        Long loggedUserId = jwtUtil.getLoggedInUser().getId();

        if (!userId.equals(loggedUserId)) {
            System.out.println("Unauhtorized");
            return null;
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

    private Trip sendTripForAdminApproval(Trip existingTrip, TripStatus status) {
        if (existingTrip.getStatus() == TripStatus.WAITING_FOR_APPROVAL && existingTrip.getStatus() == TripStatus.APPROVED) {
            System.out.println("Cannot change status to Waiting for Approval");
            return null;
        } else {
            existingTrip.setStatus(status);
            return existingTrip;
        }
    }

    private Trip approveTrip(Trip existingTrip, TripStatus status) {
        if (existingTrip.getStatus() == TripStatus.CREATED && existingTrip.getStatus() == TripStatus.APPROVED) {
            System.out.println("Cannot change status to Approved");
            return null;
        }

        if (!jwtUtil.loggedUserIsAdmin()) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }

        existingTrip.setStatus(status);
        return existingTrip;

    }

    private Trip rejectTrip(Trip existingTrip, TripStatus status) {

        if (!jwtUtil.loggedUserIsAdmin()) {
            System.out.println("You do not have admin rights!");
            return null;
        }

        if (existingTrip.getStatus() != TripStatus.WAITING_FOR_APPROVAL) {
            System.out.println("The trip has not been sent for submission yet!");
            return null;
        }
        existingTrip.setStatus(status);
        return existingTrip;
    }

}
