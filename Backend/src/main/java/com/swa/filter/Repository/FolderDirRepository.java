package com.swa.filter.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swa.filter.mySQLTables.FolderDir;

public interface FolderDirRepository extends JpaRepository<FolderDir,Integer>{
    Optional<FolderDir>findByName(String name);
    Optional<FolderDir>findByPath(String name);
}
