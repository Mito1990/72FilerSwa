package com.swa.filter.mySQLTables;

import java.util.Date;

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
public class FolderDir {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int folder_id;
    String name;
    String path;
    Long size;
    Date date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id")
    HomeDir home;
}
