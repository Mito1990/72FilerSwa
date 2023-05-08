package com.swa.filter.mySQLTables;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id_file;
    String name;
    String path;
    Long size;
    Date date;
}
