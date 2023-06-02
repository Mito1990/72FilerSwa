package com.swa.filter.mySQLTables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.swa.filter.ObjectModel.Role;

@Entity
@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class MyGroups {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int group_id;
    private String name;
    private String admin;
    private String guest;
    private Role role;
    @OneToMany
    private List<MyGroupMembers>members;
    @OneToMany
    private List<FolderDir>sharedFolders;
}
