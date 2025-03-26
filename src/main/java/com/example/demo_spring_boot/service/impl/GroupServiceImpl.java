package com.example.demo_spring_boot.service.impl;

import com.example.demo_spring_boot.dto.GroupDto;
import com.example.demo_spring_boot.model.entity.Group;
import com.example.demo_spring_boot.repository.GroupRepository;
import com.example.demo_spring_boot.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<GroupDto> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(group -> new GroupDto(group.getId(), group.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public GroupDto getGroupById(Long id) {
        Group group = groupRepository.findById(id).orElseThrow();
        return new GroupDto(group.getId(), group.getName());
    }

    @Override
    public GroupDto createGroup(GroupDto groupDto) {
        Group group = new Group();
        group.setName(groupDto.getName());
        return new GroupDto(groupRepository.save(group).getId(), group.getName());
    }

    @Override
    public GroupDto updateGroup(Long id, GroupDto groupDto) {
        Group group = groupRepository.findById(id).orElseThrow();
        group.setName(groupDto.getName());
        return new GroupDto(groupRepository.save(group).getId(), group.getName());
    }

    @Override
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
}