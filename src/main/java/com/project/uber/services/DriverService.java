package com.project.uber.services;

import com.project.uber.dto.DriverDto;
import com.project.uber.dto.RideDto;
import com.project.uber.dto.RiderDto;
import com.project.uber.entities.Driver;

import java.util.List;

public interface DriverService {
    RideDto acceptRide(Long rideRequestId);
    RideDto cancelRide(Long rideId);
    RideDto startRide(Long rideId, String otp);
    RideDto endRide(Long rideId);
    RiderDto rateRider(Long rideId, Integer rating);
    DriverDto getMyProfile();
    List<RideDto> getAllMyRides();
    Driver getCurrentDriver();
}
