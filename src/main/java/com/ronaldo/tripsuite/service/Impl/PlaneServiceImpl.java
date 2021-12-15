package com.ronaldo.tripsuite.service.Impl;

import com.ronaldo.tripsuite.entity.Plane;
import com.ronaldo.tripsuite.repository.PlaneRepository;
import com.ronaldo.tripsuite.service.PlaneService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PlaneServiceImpl implements PlaneService {

    private final PlaneRepository planeRepository;

    public PlaneServiceImpl(PlaneRepository planeRepository) {
        this.planeRepository = planeRepository;
    }

    @Override
    public Plane findById(String id) {
        Optional<Plane> planeOptional = planeRepository.findById(id);

        if (planeOptional.isEmpty()) {
            throw new NoSuchElementException("There is not plane with this ID");
        }

        return planeOptional.get();
    }
}
