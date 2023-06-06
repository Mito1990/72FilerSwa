package com.swa.filter.mySQLTables;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Folder extends FileElement {
    @OneToMany
    private List<FileElement> children;
    public Folder(String name, Integer parentFolderID, Boolean isShared,Boolean isFile) {
        super(null, name, parentFolderID, isShared,isFile, null);
        this.children = new ArrayList<>();
    }
}