package com.example.demo_spring_boot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionDto {
  private Long id;
  private String name;

  public PermissionDto(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}