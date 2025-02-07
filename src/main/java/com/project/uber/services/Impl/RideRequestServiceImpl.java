package com.project.uber.services.Impl;

import com.project.uber.entities.RideRequest;
import com.project.uber.exceptions.ResourceNotFoundException;
import com.project.uber.repositories.RideRequestRepository;
import com.project.uber.services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {

    private final RideRequestRepository rideRequestRepository;

    @Override
    public RideRequest findRideRequestById(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("RideRequest not found with ID: " + rideRequestId)
                );
    }

    @Override
    public void update(RideRequest rideRequest) {
        rideRequestRepository.findById(rideRequest.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("RideRequest not found with ID: " + rideRequest.getId())
                );
        rideRequestRepository.save(rideRequest);
    }
}
