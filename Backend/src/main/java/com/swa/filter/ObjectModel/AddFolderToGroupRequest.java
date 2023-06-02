
package com.swa.filter.ObjectModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddFolderToGroupRequest {
    String token;
    int groupID;
    int parentID;
    String Path;
    String name;
    boolean shared;
}