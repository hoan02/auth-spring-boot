package com.example.demo_spring_boot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo_spring_boot.model.entity.Group;
import com.example.demo_spring_boot.model.entity.User;

public interface GroupRepository extends JpaRepository<Group, Long> {
  Optional<Group> findByName(String name);

  List<Group> findByUser(User user);
}
