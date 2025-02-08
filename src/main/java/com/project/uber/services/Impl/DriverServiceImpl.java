package com.project.uber.services.Impl;

import com.project.uber.dto.DriverDto;
import com.project.uber.dto.RideDto;
import com.project.uber.dto.RiderDto;
import com.project.uber.entities.Driver;
import com.project.uber.entities.Ride;
import com.project.uber.entities.RideRequest;
import com.project.uber.entities.enums.RideRequestStatus;
import com.project.uber.entities.enums.RideStatus;
import com.project.uber.exceptions.ResourceNotFoundException;
import com.project.uber.repositories.DriverRepository;
import com.project.uber.services.DriverService;
import com.project.uber.services.PaymentService;
import com.project.uber.services.RideRequestService;
import com.project.uber.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final RideRequestService rideRequestService;
    private final DriverRepository driverRepository;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public RideDto acceptRide(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);
        if(!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)){
            throw new RuntimeException("RideRequest can not be accepted, status is " + rideRequest.getRideRequestStatus());
        }

        Driver currentDriver = getCurrentDriver();
        if(!currentDriver.getAvailable()){
            throw new RuntimeException("Driver cannot accept the ride, due to unavailability");
        }

        Driver savedDriver = updateDriverAvailability(currentDriver, false);
        Ride ride = rideService.createNewRide(rideRequest, savedDriver);
        return modelMapper.map(ride, RideDto.class);
    }

    @Override
    @Transactional
    public RideDto cancelRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver currentDriver = getCurrentDriver();

        if(!currentDriver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot cancel the Ride, because this is not the driver who accept it earlier.");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride cannot be cancelled, invalid status: " + ride.getRideStatus());
        }

        rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        updateDriverAvailability(currentDriver, true);

        return modelMapper.map(ride, RideDto.class);
    }

    @Override
    @Transactional
    public RideDto startRide(Long rideId, String otp) {
        Ride ride = rideService.getRideById(rideId);
        Driver currentDriver = getCurrentDriver();

        if(!currentDriver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start a ride as he has not accepted the ride earlier");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride status is not confirmed hence it can not get started, status: " + ride.getRideStatus());
        }

        if(!otp.equals(ride.getOtp())){
            throw new RuntimeException("OTP is invalid: " + otp);
        }

        ride.setStartedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ONGOING);

        // create payment associated with this ride
        paymentService.createNewPayment(savedRide);

        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    @Transactional
    public RideDto endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver currentDriver = getCurrentDriver();

        if(!currentDriver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start a ride as he has not accepted the ride earlier");
        }

        if(!ride.getRideStatus().equals(RideStatus.ONGOING)){
            throw new RuntimeException("Ride status is not ongoing hence it can not get ended, status: " + ride.getRideStatus());
        }
        // update ride status as ENDED
        ride.setEndedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ENDED);

        // update drivers status as available
        updateDriverAvailability(currentDriver, true);

        // process payment
        paymentService.processPayment(ride);
        
        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        return null;
    }

    @Override
    public DriverDto getMyProfile() {
        Driver currentDriver = getCurrentDriver();
        return modelMapper.map(currentDriver, DriverDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Driver currentDriver = getCurrentDriver();
        return rideService.getAllRidersOfDriver(currentDriver,  pageRequest).map(
                ride -> modelMapper.map(ride, RideDto.class)
        );
    }

    @Override
    public Driver getCurrentDriver() {
        // TODO: implement spring security
        return driverRepository.findById(2L)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Driver not found with id: " + 2)
                );
    }

    @Override
    public Driver updateDriverAvailability(Driver driver, boolean available) {
        driver.setAvailable(available);
        return driverRepository.save(driver);
    }
}
