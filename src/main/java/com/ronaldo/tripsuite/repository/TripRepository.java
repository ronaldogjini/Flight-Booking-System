package com.ronaldo.tripsuite.repository;

import com.ronaldo.tripsuite.entity.Role;
import com.ronaldo.tripsuite.entity.Trip;
import com.ronaldo.tripsuite.enums.TripReason;
import com.ronaldo.tripsuite.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {

    Optional<Trip> findById(Long id);

    List<Trip> findByUserId(Long id);

    List<Trip> findByUserIdAndStatus(Long userId, TripStatus status);

    List<Trip> findByUserIdAndReason(Long userId, TripReason reason);

    List<Trip> findByUserIdAndReasonAndStatus(Long userId, TripReason reason, TripStatus status);

    @Modifying
    @Transactional
    @Query("update Trip t set t.deleted = 1 where t.id = ?1")
    void softDelete(Long id);

}
