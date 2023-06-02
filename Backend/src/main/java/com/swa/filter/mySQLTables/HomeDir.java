package com.swa.filter.mySQLTables;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class HomeDir {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long home_id;
    String name;
    String path;
    Long size;
    Date date;
    @OneToMany
    private List<FolderDir>folders;
}
