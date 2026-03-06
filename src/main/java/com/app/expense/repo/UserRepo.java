package com.app.expense.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.expense.entity.User;

public interface UserRepo extends JpaRepository<User, Integer>{

	Optional<User> findByEmail(String username);

}
