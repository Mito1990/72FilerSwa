package com.swa.filter.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.swa.filter.mySQLTables.MyGroups;

public interface MyGroupRepository extends JpaRepository<MyGroups, Integer> {
    MyGroups findByGroupname(String groupName);
    MyGroups findByMembers(String info);
    MyGroups findByGroupnameAndAdmin(String groupname,String username);
    void deleteByMembers(String userName);
}
