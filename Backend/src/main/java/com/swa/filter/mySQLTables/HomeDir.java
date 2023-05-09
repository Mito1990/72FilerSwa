package com.swa.filter.mySQLTables;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
// import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class HomeDir {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long home_id;
    String name;
    String path;
    Long size;
    Date date;
    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FolderDir>folder = new ArrayList<>();
    @OneToOne(mappedBy = "home", cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;
}
