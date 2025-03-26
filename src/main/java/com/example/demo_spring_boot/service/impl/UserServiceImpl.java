package com.example.demo_spring_boot.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo_spring_boot.repository.UserRepository;
import com.example.demo_spring_boot.service.UserService;
import com.example.demo_spring_boot.model.entity.User;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User registerUser(String username, String password, String email) {
    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setEmail(email);
    return userRepository.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User not found");
    }

    List<GrantedAuthority> authorities = user.get().getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());

    return new org.springframework.security.core.userdetails.User( // 👈 Chỉ định rõ ràng
        user.get().getUsername(),
        user.get().getPassword(),
        authorities);
  }

}
