package com.broadenway.ureasolution.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.broadenway.ureasolution.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByName(String name);

	User findByEmployeeId(int employeeId);
}
