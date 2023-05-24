package com.swa.filter.mySQLTables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.swa.filter.ObjectModel.Role;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long user_id;
  @NonNull
  private String name;
  @NonNull
  private String username;
  @NonNull
  private String password;
  @Enumerated(EnumType.STRING)
  private Role role;
  @OneToOne
  // @JoinColumn(name = "home_id")
  HomeDir home;
  @OneToMany
  private List<MyGroups>mygroups;

  @Override 
  public Collection<? extends GrantedAuthority> getAuthorities() {
   return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}

