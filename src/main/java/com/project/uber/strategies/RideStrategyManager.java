package com.project.uber.strategies;

import com.project.uber.strategies.Impl.DriverMatchingHighestRatedStrategy;
import com.project.uber.strategies.Impl.DriverMatchingNearestDriverStrategy;
import com.project.uber.strategies.Impl.RideFareDefaultFareCalculationStrategy;
import com.project.uber.strategies.Impl.RideFareSurgePricingFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {
    private final DriverMatchingHighestRatedStrategy highestRatedDriverStrategy;
    private final DriverMatchingNearestDriverStrategy nearestDriverStrategy;
    private final RideFareDefaultFareCalculationStrategy defaultFareCalculationStrategy;
    private final RideFareSurgePricingFareCalculationStrategy surgePricingFareCalculationStrategy;

    public DriverMatchingStrategy driverMatchingStrategy(double riderRating){
        if(riderRating >= 4.8){
            return highestRatedDriverStrategy;
        } else {
            return nearestDriverStrategy;
        }
    }

    public RideFareCalculationStrategy rideFareCalculationStrategy(){
        // 6 PM - 9 PM
        LocalTime surgeStartTime = LocalTime.of(18, 0);
        LocalTime surgeEndTime = LocalTime.of(21, 0);
        LocalTime currentTime = LocalTime.now();

        boolean isSurgedTime = currentTime.isBefore(surgeStartTime) && currentTime.isAfter(surgeEndTime);

        if(isSurgedTime){
            return surgePricingFareCalculationStrategy;
        } else {
            return defaultFareCalculationStrategy;
        }
    }
}
