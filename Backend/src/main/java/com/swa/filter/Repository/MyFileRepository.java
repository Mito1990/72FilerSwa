package com.swa.filter.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swa.filter.mySQLTables.Folder;
import com.swa.filter.mySQLTables.MyFile;

public interface MyFileRepository extends JpaRepository<MyFile,Integer>{
    Optional<MyFile>findByName(String name);
    Optional<MyFile>findByParent(Folder parent);
}
