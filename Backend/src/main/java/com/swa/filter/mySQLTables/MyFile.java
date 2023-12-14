package com.swa.filter.mySQLTables;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Builder
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MyFile extends FileElement {
    private String extension;
    public MyFile(String name, Integer parentFolderID, Boolean isShared, Boolean isFile) {
        super(null, name, parentFolderID, isShared, isFile,null);
        this.extension = ".txt";
    }

}
