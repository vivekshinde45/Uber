package com.project.uber.controllers;

import com.project.uber.dto.*;
import com.project.uber.services.DriverService;
import com.project.uber.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;
    private final RatingService ratingService;

    @PostMapping("/acceptRide/{rideRequestId}")
    public ResponseEntity<RideDto> acceptRide(@PathVariable Long rideRequestId){
        return ResponseEntity.ok(driverService.acceptRide(rideRequestId));
    }

    @PostMapping("/startRide/{rideRequestId}")
    public ResponseEntity<RideDto> startRide(@PathVariable Long rideRequestId, @RequestBody RideStartDto rideStartDto){
        return ResponseEntity.ok(driverService.startRide(rideRequestId, rideStartDto.getOtp()));
    }

    @PostMapping("/endRide/{rideId}")
    public ResponseEntity<RideDto> endRide(@PathVariable Long rideId){
        return ResponseEntity.ok(driverService.endRide(rideId));
    }

    @PostMapping("/rateRider")
    public ResponseEntity<RiderDto> rateRider(@RequestBody RatingDto ratingDto){
        return ResponseEntity.ok(driverService.rateRider(ratingDto.getRideId(), ratingDto.getRating()));
    }

    @GetMapping("/getMyProfile")
    public ResponseEntity<DriverDto> getMyProfile(){
        return ResponseEntity.ok(driverService.getMyProfile());
    }

    @GetMapping("/getAllMyRides")
    public ResponseEntity<Page<RideDto>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                       @RequestParam(defaultValue = "10") Integer pageSize){
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "createdAt", "id"));
        return ResponseEntity.ok(driverService.getAllMyRides(pageRequest));
    }

//    @PostMapping("/rateRider/{rideId}/{rating}")
//    private ResponseEntity<RiderDto> rateRider(@PathVariable Long rideId, @PathVariable Integer rating){
//        return ResponseEntity.ok(driverService.rateRider(rideId, rating));
//    }
}
