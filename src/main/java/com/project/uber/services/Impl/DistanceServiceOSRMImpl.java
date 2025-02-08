package com.project.uber.services.Impl;

import com.project.uber.services.DistanceService;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class DistanceServiceOSRMImpl implements DistanceService {
    private static final String OSRM_API_BASE_URL = "https://router.project-osrm.org/route/v1/driving/";
    @Override
    public Double calculateDistance(Point source, Point destination) {
        try{
            String uri = source.getX() + "," + source.getY() + ";" + destination.getX() + "," + destination.getY();
            OSRMResponseDto responseDto = RestClient.builder()
                    .baseUrl(OSRM_API_BASE_URL)
                    .build()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .body(OSRMResponseDto.class);
            return responseDto.getRoutes().get(0).getDistance() / 1000.0;
        }
        catch (Exception ex){
            throw new RuntimeException("Error getting data from OSRM " + ex.getMessage());
        }
    }
}

@Data
class OSRMResponseDto{
    private List<OSRMRoute> routes;
}

@Data
class OSRMRoute{
    private Double distance;
}
