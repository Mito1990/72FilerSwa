package com.swa.filter.ObjectModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor 
public class NewFolderGroupRequest {
    String name;
    int groupID;
    int folderID;
    int parent;
    String path;
    String token;
    boolean shared;
}
