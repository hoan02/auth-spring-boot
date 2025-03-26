package com.example.demo_spring_boot.service;

import java.util.Optional;

import com.example.demo_spring_boot.model.entity.Group;

public interface GroupService {
  Group createGroup(String name, String description);
  Optional<Group> getGroupByName(String name);
}
