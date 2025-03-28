package com.example.demo_spring_boot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupDto {
  private Long id;
  private String name;

  public GroupDto(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}