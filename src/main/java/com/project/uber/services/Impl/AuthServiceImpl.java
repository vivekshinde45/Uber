package com.project.uber.services.Impl;

import com.project.uber.dto.DriverDto;
import com.project.uber.dto.SignUpDto;
import com.project.uber.dto.UserDto;
import com.project.uber.entities.User;
import com.project.uber.entities.enums.Role;
import com.project.uber.exceptions.RuntimeConflictException;
import com.project.uber.repositories.UserRepository;
import com.project.uber.services.AuthService;
import com.project.uber.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RiderService riderService;

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
        mappedUser.setRoles(Set.of(Role.RIDER));
        User savedUser = userRepository.save(mappedUser);

        // user is RIDER by default
        // create user related entities such as Rider, Wallet
        riderService.createNewRider(savedUser);

        // TODO: add wallet for every new user ONCE wallet service is created

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public DriverDto onboardNewDriver(Long userId) {
        return null;
    }
}
