package com.swa.filter.ObjectModel;

import com.swa.filter.mySQLTables.Folder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetFolderRequest {
    String token;
    Integer parentID;
}
