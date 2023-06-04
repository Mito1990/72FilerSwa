package com.swa.filter.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swa.filter.mySQLTables.Folder;

public interface FolderRepository extends JpaRepository<Folder,Integer>{
    Optional<Folder>findByName(String name);
    Optional<Folder>findByParent(Folder parent);
}
