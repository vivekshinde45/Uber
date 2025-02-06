package com.project.uber.services.Impl;

import com.project.uber.dto.DriverDto;
import com.project.uber.dto.RideDto;
import com.project.uber.dto.RideRequestDto;
import com.project.uber.dto.RiderDto;
import com.project.uber.entities.Driver;
import com.project.uber.entities.RideRequest;
import com.project.uber.entities.Rider;
import com.project.uber.entities.User;
import com.project.uber.entities.enums.RideRequestStatus;
import com.project.uber.exceptions.ResourceNotFoundException;
import com.project.uber.repositories.RideRequestRepository;
import com.project.uber.repositories.RiderRepository;
import com.project.uber.services.RiderService;
import com.project.uber.strategies.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiderServiceImpl implements RiderService {
    private final ModelMapper modelMapper;
    private final RideStrategyManager rideStrategyManager;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;

    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        Rider rider = getCurrentRider();
        // Map the DTO to entity
        RideRequest rideRequest = modelMapper.map(rideRequestDto, RideRequest.class);
        log.info("checking the ride request object " + rideRequest.toString());

        // set ride request status to PENDING
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        // set the rider to ride_request
        rideRequest.setRider(rider);

        // calculate the fare
        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare);

        // save the ride request
        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        // try to find the driver match
        List<Driver> matchingDrivers = rideStrategyManager.driverMatchingStrategy(rider.getRatings()).findMatchingDrivers(rideRequest);

        return  modelMapper.map(savedRideRequest, RideRequestDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        return null;
    }

    @Override
    public DriverDto rateDriver(Long rideId, Integer rating) {
        return null;
    }

    @Override
    public RiderDto getMyProfile() {
        return null;
    }

    @Override
    public List<RideDto> getAllMyRides() {
        return null;
    }

    @Override
    public Rider createNewRider(User user) {
        Rider rider = Rider
                .builder()
                .user(user)
                .ratings(0.0)
                .build();
        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {
        // TODO: implement spring security
        return riderRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException(
                "Rider not found with id: " + 1
        ));
    }
}
