package com.example.demo_spring_boot.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo_spring_boot.model.entity.Group;
import com.example.demo_spring_boot.model.entity.Role;
import com.example.demo_spring_boot.model.entity.User;

public interface GroupRepository extends JpaRepository<Group, Long> {
  Optional<Group> findByName(String name);

  @Query("SELECT g FROM Group g JOIN g.users u WHERE u = :user")
  List<Group> findByUser(@Param("user") User user);

  @Query("SELECT g FROM Group g JOIN g.roles r WHERE r IN (:roles)")
  List<Group> findByRoles(@Param("roles") Set<Role> roles);
}
