package com.example.demo_spring_boot.service;

import java.util.List;

import com.example.demo_spring_boot.dto.UserDto;

public interface UserService {

  List<UserDto> getAllUsers();

  UserDto getUserById(Long id);

  UserDto createUser(UserDto userDto);

  UserDto updateUser(Long id, UserDto userDto);

  void deleteUser(Long id);
}
