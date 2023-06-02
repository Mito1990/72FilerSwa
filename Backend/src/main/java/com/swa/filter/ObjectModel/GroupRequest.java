package com.swa.filter.ObjectModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequest {
    private String name;
    private Integer groupID;
    private int folderID;
    private int parent;
    private String path;
    private String token;
    private boolean shared;
    private boolean file;
}
