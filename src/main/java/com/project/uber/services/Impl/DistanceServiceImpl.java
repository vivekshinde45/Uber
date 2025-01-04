package com.project.uber.services.Impl;

import com.project.uber.services.DistanceService;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
public class DistanceServiceImpl implements DistanceService {
    @Override
    public Double calculateDistance(Point source, Point destination) {
        return null;
    }
}
