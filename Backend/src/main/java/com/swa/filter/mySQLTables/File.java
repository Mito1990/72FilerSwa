package com.swa.filter.mySQLTables;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
public class File extends FileElement {
    private String extension;
    public File(String name, Folder parent, boolean isShared) {
        super(name, isShared);
        this.extension = ".txt";
    }
}
