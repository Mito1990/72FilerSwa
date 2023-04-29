package com.swa.filter.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.swa.filter.mySQLTables.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUserName(String userName);
}