package com.swa.filter.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swa.filter.mySQLTables.Member;

public interface MemberRepository extends JpaRepository<Member,Integer> {
}
