package com.project.uber.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PointDto {
    private double[] coordinates;
    private String type = "point";

    public PointDto(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
