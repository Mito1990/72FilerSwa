package com.swa.filter.mySQLTables;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Folder extends FileElement {
    public Folder(String name, Folder parent, boolean isShared) {
        super(name,isShared);
        this.children = new ArrayList<>();
    }
    @OneToMany
    private List<FileElement>children;
}
