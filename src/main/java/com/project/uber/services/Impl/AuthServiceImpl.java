package com.project.uber.services.Impl;

import com.project.uber.dto.DriverDto;
import com.project.uber.dto.SignUpDto;
import com.project.uber.dto.UserDto;
import com.project.uber.entities.Driver;
import com.project.uber.entities.User;
import com.project.uber.entities.enums.Role;
import com.project.uber.exceptions.ResourceNotFoundException;
import com.project.uber.exceptions.RuntimeConflictException;
import com.project.uber.repositories.UserRepository;
import com.project.uber.services.AuthService;
import com.project.uber.services.DriverService;
import com.project.uber.services.RiderService;
import com.project.uber.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.project.uber.entities.enums.Role.DRIVER;
import static com.project.uber.entities.enums.Role.RIDER;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RiderService riderService;
    private final WalletService walletService;
    private final DriverService driverService;

    @Override
    public String login(String email, String password) {
        return null;
    }

    @Override
    @Transactional
    public UserDto signUp(SignUpDto signUpDto) {
        // everyone is the RIDER at first as by default

        // every email should be unique
        User user = userRepository.findByEmail(signUpDto.getEmail()).orElseThrow(() ->
                new RuntimeConflictException("Cannot SignUp, User already exist with emailID: " + signUpDto.getEmail()));

        // create a new user with RIDER role
        User mappedUser = modelMapper.map(signUpDto, User.class);
        mappedUser.setRoles(Set.of(RIDER));
        User savedUser = userRepository.save(mappedUser);

        // user is RIDER by default
        // create user related entities such as Rider, Wallet
        riderService.createNewRider(savedUser);
        walletService.createNewWallet(savedUser);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    @Transactional
    public DriverDto onboardNewDriver(Long userId, String vehicleId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with ID: " + userId)
        );
        if(user.getRoles().contains(DRIVER))
            throw new RuntimeConflictException("User with userId: " + userId + " is already a Driver");
        user.getRoles().add(DRIVER);

        Driver driver = Driver.builder()
                .user(user)
                .available(true)
                .ratings(0.0)
                .vehicleId(vehicleId)
                .build();

        userRepository.save(user);
        Driver savedDriver = driverService.createNewDriver(driver);
        return modelMapper.map(savedDriver, DriverDto.class);
    }
}
