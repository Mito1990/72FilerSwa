package com.swa.filter.mySQLTables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class MyGroups {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int group_id;
    private String groupName;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "info",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name="info_id"))
    private List<UserGroupInfo>info = new ArrayList<>();
}
