package com.swa.filter.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.swa.filter.mySQLTables.FileElement;
public interface FileElementRepository extends JpaRepository<FileElement,Integer>{

}

