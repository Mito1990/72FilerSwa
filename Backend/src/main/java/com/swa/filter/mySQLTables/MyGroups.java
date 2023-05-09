package com.swa.filter.mySQLTables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import com.swa.filter.mySQLTables.MyGroupMembers;

import java.util.List;

import com.swa.filter.ObjectModel.Role;

@Entity
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class MyGroups {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int group_id;
    private String groupname;
    private String admin;
    private Role role;
    // @OneToMany(cascade = CascadeType.ALL)
    // @JoinTable(name = "members",
    //         joinColumns = @JoinColumn(name = "group_id"),
    //         inverseJoinColumns = @JoinColumn(name="member_id"))
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MyGroupMembers>members;
}
