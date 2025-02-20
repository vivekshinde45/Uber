package com.project.uber.services;

import com.project.uber.dto.DriverDto;
import com.project.uber.dto.SignUpDto;
import com.project.uber.dto.UserDto;

public interface AuthService {
    String[] login(String email, String password);
    UserDto signUp(SignUpDto signUpDto);
    DriverDto onboardNewDriver(Long userId, String vehicleId);
    String refreshToken(String refreshToken);
}
