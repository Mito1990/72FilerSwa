package com.swa.filter.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.swa.filter.mySQLTables.HomeDir;

public interface HomeDirRepository extends JpaRepository<HomeDir,Long>{
    // HomeDir findByName(String username);
    // HomeDir findbyPath(String username);
}
