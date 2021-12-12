package com.ronaldo.tripsuite.repository;

import com.ronaldo.tripsuite.entity.Plane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaneRepository extends JpaRepository<Plane, String> {

    Optional<Plane> findById(String id);
}
