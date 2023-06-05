package com.swa.filter.mySQLTables;

import jakarta.persistence.Entity;

@Entity
public class File extends FileElement {
    private String extension;
    public File(String name, Integer parentFolderID, boolean isShared) {
        super(null, name, parentFolderID, isShared);
        this.extension = ".txt";
    }
}

// @Entity
// public class File extends FileElement {
//     private String extension;

//     public File(String name, Integer parentFolderID, boolean isShared) {
//         super(null, name, parentFolderID, isShared);
//         this.extension = ".txt";
//     }
// }