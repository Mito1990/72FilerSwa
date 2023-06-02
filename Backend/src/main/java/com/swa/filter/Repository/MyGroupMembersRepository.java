package com.swa.filter.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swa.filter.mySQLTables.MyGroupMembers;

public interface MyGroupMembersRepository extends JpaRepository<MyGroupMembers,Integer> {
}
