package com.swa.filter.ObjectModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewFileRequest {
    String token;
    String fileName;
    String groupName;
    Integer memberGroupID;
    Integer parentFolderID;
    Boolean isShared;
}
