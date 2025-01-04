package com.project.uber.dto;

import com.project.uber.entities.enums.PaymentMethod;
import com.project.uber.entities.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDto {
    private Long id;
    private Point pickUpLocation;
    private Point dropOffLocation;
    private LocalDateTime createdAt; // Driver accepts the RIDE
    private LocalDateTime startedAt; // Driver starts the RIDE
    private LocalDateTime endedAt;   // Driver ended the RIDE
    private RiderDto rider;
    private DriverDto driver;
    private PaymentMethod paymentMethod;
    private RideStatus rideStatus;
    private String otp;
}
