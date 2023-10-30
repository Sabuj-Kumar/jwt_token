package com.springJWTProject.springSecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springJWTProject.springSecurity.entities.User;
import com.springJWTProject.springSecurity.enums.Role;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{

	Optional<User> findByEmail(String email);
	
	User findByRole(Role role);
}
