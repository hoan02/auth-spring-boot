package com.example.demo_spring_boot.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  private String password;
  private String email;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_groups", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
  private Set<Group> groups = new HashSet<>();
}