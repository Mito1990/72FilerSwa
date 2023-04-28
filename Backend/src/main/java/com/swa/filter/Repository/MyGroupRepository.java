package com.swa.filter.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.User;

public interface MyGroupRepository extends JpaRepository<MyGroups, Integer> {
    MyGroups findByGroupName(String groupName);
    MyGroups findByUsers(User user);
    void deleteByUser(User user);
}
