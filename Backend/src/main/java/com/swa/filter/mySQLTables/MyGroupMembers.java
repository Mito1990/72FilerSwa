package com.swa.filter.mySQLTables;

import com.swa.filter.ObjectModel.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class MyGroupMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int member_id;
    private int groupID;
    private String username;
    private Role role;
}
