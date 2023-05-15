package com.swa.filter.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swa.filter.mySQLTables.FolderDir;

public interface FolderDirRepository extends JpaRepository<FolderDir,Long>{
    Optional<FolderDir>findByName(String name);
    Optional<FolderDir>findByPath(String name);
}
