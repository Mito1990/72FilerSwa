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
    private int groupID;
    private int parent;
    private String path;
    private String token;
    private boolean shared;
}
