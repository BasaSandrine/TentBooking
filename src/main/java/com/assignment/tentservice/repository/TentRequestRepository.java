package com.assignment.tentservice.repository;

import com.assignment.tentservice.model.TentRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface TentRequestRepository extends JpaRepository<TentRequest,Integer> {
}
