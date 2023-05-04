package com.swa.filter.mySQLTables;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class MyGroupDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int info_id;
    private String groupname;
    private String username;
    private String role;
}
