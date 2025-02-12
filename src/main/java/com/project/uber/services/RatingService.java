package com.project.uber.services;

import com.project.uber.dto.DriverDto;
import com.project.uber.dto.RiderDto;
import com.project.uber.entities.Ride;

public interface RatingService {
    DriverDto rateDriver(Ride ride, Integer rating);
    RiderDto rateRider(Ride ride, Integer rating);
    void createNewRating(Ride ride);
}
