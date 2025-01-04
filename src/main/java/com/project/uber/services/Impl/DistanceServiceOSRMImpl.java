package com.project.uber.services.Impl;

import com.project.uber.services.DistanceService;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
public class DistanceServiceOSRMImpl implements DistanceService {
    @Override
    public Double calculateDistance(Point source, Point destination) {
        // TODO: call the OSRM API to get the distance
        return null;
    }
}
