package com.example.demo_spring_boot.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo_spring_boot.dto.UserDto;
import com.example.demo_spring_boot.model.entity.User;
import com.example.demo_spring_boot.repository.UserRepository;
import com.example.demo_spring_boot.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getPassword()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        List<String> errors = new ArrayList<>();
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            errors.add("Username đã tồn tại");
        }
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            errors.add("Email đã tồn tại");
        }
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.join(", ", errors));
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return new UserDto(userRepository.save(user).getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return new UserDto(userRepository.save(user).getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}