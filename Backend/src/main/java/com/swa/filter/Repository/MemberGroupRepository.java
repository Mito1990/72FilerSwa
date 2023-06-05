package com.swa.filter.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swa.filter.mySQLTables.MemberGroup;

public interface MemberGroupRepository extends JpaRepository<MemberGroup,Integer> {
}
