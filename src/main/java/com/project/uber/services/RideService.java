package com.project.uber.services;

import com.project.uber.dto.RideRequestDto;
import com.project.uber.entities.Driver;
import com.project.uber.entities.Ride;
import com.project.uber.entities.RideRequest;
import com.project.uber.entities.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


public interface RideService {
    Ride getRideById(Long rideId);
    void matchWithDrivers(RideRequestDto rideRequestDto);
    Ride createNewRide(RideRequest rideRequest, Driver driver);
    Ride updateRideStatus(Ride ride, RideStatus rideStatus);
    Page<Ride> getAllRidesOfRider(Long riderId, PageRequest pageRequest);
    Page<Ride> getAllRidersOfDriver(Long driverId, PageRequest pageRequest);
}
