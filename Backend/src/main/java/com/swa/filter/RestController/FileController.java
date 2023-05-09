package com.swa.filter.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.swa.filter.ObjectModel.GetFolderRequest;
import com.swa.filter.ObjectModel.NewFolderRequest;
import com.swa.filter.Services.FileService;
import com.swa.filter.mySQLTables.FolderDir;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folder")
public class FileController {
    private final FileService fileService;

    @GetMapping("/get")
    public FolderDir getFolder(@RequestBody GetFolderRequest getFolderRequest){
        return fileService.getFolder(getFolderRequest);
    }
    @PostMapping("/new")
    public ResponseEntity<?> newFolder(@RequestBody NewFolderRequest newFolderRequest){
        return ResponseEntity.ok().body(fileService.newFolder(newFolderRequest));
    }

    // @PostMapping(path = "/create/group")
    // public  createGroup(@RequestBody GroupRequest groupRequest){
    //   return ResponseEntity.ok().body(myGroupService.createGroup(groupRequest));
    // }  
    // @Autowired
    // private HomeDirRepository homeDirRepository;
    // @GetMapping("/folder/{folderId}")
    // public Filer getFolder(@PathVariable Long folderId) {
    //     homeDirRepository.findById(folderId);
    //     return null;
    // }
}
