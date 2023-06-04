package com.swa.filter.ObjectModel;
import com.swa.filter.mySQLTables.Folder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddNewFolderRequest {
    String token;
    String name;
    Folder parent;
    boolean isShared;
}
