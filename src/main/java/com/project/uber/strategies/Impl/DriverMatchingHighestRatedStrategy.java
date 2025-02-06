package com.project.uber.strategies.Impl;

import com.project.uber.entities.Driver;
import com.project.uber.entities.RideRequest;
import com.project.uber.repositories.DriverRepository;
import com.project.uber.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverMatchingHighestRatedStrategy implements DriverMatchingStrategy {
    private final DriverRepository driverRepository;

    @Override
    public List<Driver> findMatchingDrivers(RideRequest rideRequest) {
        return driverRepository.findTenNearbyTopRatedDrivers(rideRequest.getPickUpLocation());
    }
}
