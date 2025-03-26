package com.example.demo_spring_boot.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo_spring_boot.dto.UserDto;
import com.example.demo_spring_boot.model.entity.User;
import com.example.demo_spring_boot.repository.UserRepository;
import com.example.demo_spring_boot.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return new UserDto(userRepository.save(user).getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return new UserDto(userRepository.save(user).getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}