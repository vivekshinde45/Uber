package com.project.uber.strategies.Impl;

import com.project.uber.entities.RideRequest;
import com.project.uber.services.DistanceService;
import com.project.uber.strategies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFareSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {

    private final DistanceService distanceService;
    private static final double SURGE_FACTOR = 2;
    @Override
    public double calculateFare(RideRequest rideRequest) {
        double distance = distanceService.calculateDistance(rideRequest.getPickUpLocation(), rideRequest.getDropOffLocation());
        return distance * RIDE_FARE_MULTIPLIER * SURGE_FACTOR;
    }
}
