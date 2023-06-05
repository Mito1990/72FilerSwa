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

@EqualsAndHashCode(callSuper=false)
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Folder extends FileElement {
    @OneToMany
    private List<FileElement> children = new ArrayList<>();
    public Folder(String name, Folder parent, boolean isShared) {
        super(null, name, parent, isShared);
        this.children = new ArrayList<FileElement>();
    }
}
