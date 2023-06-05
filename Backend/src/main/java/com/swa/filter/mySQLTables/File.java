package com.swa.filter.mySQLTables;

import jakarta.persistence.Entity;

@Entity
public class File extends FileElement {
    private String extension;

    public File(String name, Folder parent, boolean isShared) {
        super(null, name, parent, isShared);
        this.extension = ".txt";
    }
}
