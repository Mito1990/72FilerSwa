package com.swa.filter.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.swa.filter.mySQLTables.MyGroups;

public interface MyGroupRepository extends JpaRepository<MyGroups, Integer> {
    MyGroups findByName(String name);
    MyGroups findByMembers(String info);
    MyGroups findByNameAndAdmin(String name,String username);
    void deleteByMembers(String userName);
}
