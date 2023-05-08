package com.swa.filter.RestController;

import javax.annotation.processing.Filer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.swa.filter.Repository.HomeDirRepository;

@RestController
public class HomeDirController {

    @Autowired
    private HomeDirRepository homeDirRepository;
    @GetMapping("/folder/{folderId}")
    public Filer getFolder(@PathVariable Long folderId) {
        homeDirRepository.findById(folderId);
        return null;
    }
}
