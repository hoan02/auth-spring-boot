package com.example.demo_spring_boot.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo_spring_boot.model.entity.Group;
import com.example.demo_spring_boot.repository.GroupRepository;
import com.example.demo_spring_boot.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {

  private final GroupRepository groupRepository;

  public GroupServiceImpl(GroupRepository groupRepository) {
    this.groupRepository = groupRepository;
  }

  @Override
  public Group createGroup(String name, String description) {
    Group group = new Group();
    group.setName(name);
    group.setDescription(description);
    return groupRepository.save(group);
  }

  @Override
  public Optional<Group> getGroupByName(String name) {
    return groupRepository.findByName(name);
  }
}
