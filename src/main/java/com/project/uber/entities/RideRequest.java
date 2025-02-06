package com.project.uber.entities;

import com.project.uber.entities.enums.PaymentMethod;
import com.project.uber.entities.enums.RideRequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class RideRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point pickUpLocation;

    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point dropOffLocation;

    @CreationTimestamp
    private LocalDateTime requestedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Rider rider;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private RideRequestStatus rideRequestStatus;

    private Double fare;

    @Override
    public String toString() {
        return "RideRequest{" +
                "id=" + id +
                ", pickUpLocation=" + pointToString(pickUpLocation) +
                ", dropOffLocation=" + pointToString(dropOffLocation) +
                ", requestedTime=" + requestedTime +
                ", riderId=" + (rider != null ? rider.getId() : "null") +
                ", paymentMethod=" + paymentMethod +
                ", rideRequestStatus=" + rideRequestStatus +
                ", fare=" + fare +
                '}';
    }

    private String pointToString(Point point) {
        if (point == null) return "null";
        return "POINT(" + point.getX() + " " + point.getY() + ")";
    }
}
