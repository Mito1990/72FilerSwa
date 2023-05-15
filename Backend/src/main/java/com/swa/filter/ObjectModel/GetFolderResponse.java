package com.swa.filter.ObjectModel;

import java.util.List;

import com.swa.filter.mySQLTables.FolderDir;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor 
public class GetFolderResponse {
    String name;
    String pathparent;
    List<FolderDir>folders;
}
