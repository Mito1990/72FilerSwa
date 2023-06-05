package com.swa.filter.mySQLTables;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MyFile extends FileElement {
    private String extension;
    public MyFile(String name, Integer parentFolderID, Boolean isShared, Boolean isFile) {
        super(null, name, parentFolderID, isShared, isFile);
        this.extension = ".txt";
    }
}
