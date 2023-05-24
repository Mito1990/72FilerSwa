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
    String groupname;
    int folderID;
    String token;    
}
