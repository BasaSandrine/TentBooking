package com.assignment.tentservice.repository;

import com.assignment.tentservice.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);
}
