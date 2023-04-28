package com.swa.filter.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.swa.filter.mySQLTables.MyGroups;

public interface MyGroupRepository extends JpaRepository<MyGroups, Integer> {
    MyGroups findByGroupName(String groupName);
    MyGroups findByUsers(String username);
}
