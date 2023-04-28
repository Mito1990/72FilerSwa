package com.swa.filter.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long user_id;
  @NonNull
  private String name;
  @NonNull
  private String username;
  @NonNull
  private String password;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "user_roles",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id"))
  private List<Role> role = new ArrayList<>();
}

