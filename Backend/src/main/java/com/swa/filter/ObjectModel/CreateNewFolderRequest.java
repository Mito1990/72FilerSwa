package com.swa.filter.ObjectModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewFolderRequest {
    String token;
    String folderName;
    Integer parentFolderID;
    Boolean isShared;
}
