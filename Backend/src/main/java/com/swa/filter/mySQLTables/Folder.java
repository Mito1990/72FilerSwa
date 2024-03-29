package com.swa.filter.mySQLTables;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Folder extends FileElement {
    @OneToMany
    // @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL)
    private List<FileElement> children;
    public Folder(String name, Integer parentFolderID, Boolean isShared,Boolean isFile) {
        super(null, name, parentFolderID, isShared,isFile, null);
        this.children = new ArrayList<>();
    }
}