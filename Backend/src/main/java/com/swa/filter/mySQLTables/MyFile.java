package com.swa.filter.mySQLTables;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MyFile extends FileElement {
    private String extension;
    private String path;
    public MyFile(String name, Integer parentFolderID, Boolean isShared, Boolean isFile) {
        super(null, name, parentFolderID, isShared, isFile);
        this.extension = ".txt";
    }
}
