package com.assignment.tentservice.repository;

import com.assignment.tentservice.model.Tent;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Transactional
public interface TentRepository extends JpaRepository<Tent,Integer> {
    //List<Car> findBySeatingCapacityAndavailableForBooking(int seatingCapacity,boolean availableForBooking);
    List<Tent> findBySeatingCapacityAndAvailableForBookingTrue(int seatingCapacity);

    List<Tent> findByIdIn(List<Integer> carIds);
}
