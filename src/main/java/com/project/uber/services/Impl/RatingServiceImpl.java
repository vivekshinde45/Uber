package com.project.uber.services.Impl;

import com.project.uber.dto.DriverDto;
import com.project.uber.dto.RiderDto;
import com.project.uber.entities.Driver;
import com.project.uber.entities.Rating;
import com.project.uber.entities.Ride;
import com.project.uber.entities.Rider;
import com.project.uber.exceptions.ResourceNotFoundException;
import com.project.uber.repositories.DriverRepository;
import com.project.uber.repositories.RatingRepository;
import com.project.uber.repositories.RiderRepository;
import com.project.uber.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;

    @Override
    public DriverDto rateDriver(Ride ride, Integer rating) {
        Driver driver = ride.getDriver();
        Rating ratingObj = ratingRepository.findByRide(ride).orElseThrow(
                () -> new ResourceNotFoundException("Rating not found for ride with ID: " + ride.getId())
        );
        ratingObj.setDriverRating(rating);

        Double newRating = ratingRepository.findByDriver(driver)
                .stream()
                .mapToDouble(Rating::getDriverRating)
                .average()
                .orElse(0.0);
        driver.setRatings(newRating);
        Driver savedDriver = driverRepository.save(driver);
        return modelMapper.map(savedDriver, DriverDto.class);
    }

    @Override
    public RiderDto rateRider(Ride ride, Integer rating) {
        Rider rider = ride.getRider();
        Rating ratingObj = ratingRepository.findByRide(ride).orElseThrow(
                () -> new ResourceNotFoundException("Rating not found for ride with ID: " + ride.getId())
        );
        ratingObj.setRiderRating(rating);

        Double newRating = ratingRepository.findByRider(rider)
                .stream()
                .mapToDouble(Rating::getRiderRating)
                .average()
                .orElse(0.0);
        rider.setRatings(newRating);
        Rider savedRider = riderRepository.save(rider);
        return modelMapper.map(savedRider, RiderDto.class);
    }

    @Override
    public void createNewRating(Ride ride) {
        Rating rating = Rating.builder()
                .rider(ride.getRider())
                .driver(ride.getDriver())
                .ride(ride)
                .build();
        ratingRepository.save(rating);
    }
}
