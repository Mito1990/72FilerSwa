package com.swa.filter.mySQLTables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private int folderID;
    private String groupname;
    private String path;
    private String admin;
    private String guest;
    private Role role;
    @OneToMany
    private List<MyGroupMembers>members;
}
