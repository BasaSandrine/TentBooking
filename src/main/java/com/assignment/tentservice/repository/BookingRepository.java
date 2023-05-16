package com.assignment.tentservice.repository;

import com.assignment.tentservice.model.Booking;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Transactional
public interface BookingRepository extends JpaRepository<Booking,Integer> {
    List<Booking> findByUsername(String username);
    void deleteByUsername(String username);
}
