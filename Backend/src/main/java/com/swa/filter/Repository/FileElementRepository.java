package com.swa.filter.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swa.filter.mySQLTables.FileElement;
import com.swa.filter.mySQLTables.Folder;

public interface FileElementRepository extends JpaRepository<FileElement,Integer>{

}

