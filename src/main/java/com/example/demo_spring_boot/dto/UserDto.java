package com.example.demo_spring_boot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
  private Long id;
  private String username;
  private String email;
}
