package com.swa.filter.ObjectModel;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberGroupRequest {
    String token;
    String user;
    String groupname;
    // int folderID;
    // String path;
    // GroupRequest groupRequest;
    // MemberRequest memberRequest;
}
