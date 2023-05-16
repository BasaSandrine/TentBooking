package com.assignment.tentservice.controller;

import com.assignment.tentservice.dao.BookingDetailDao;
import com.assignment.tentservice.model.Booking;
import com.assignment.tentservice.model.Tent;
import com.assignment.tentservice.repository.BookingRepository;
import com.assignment.tentservice.repository.TentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@SessionAttributes({"username","id"})
public class BookingController {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TentRepository tentRepository;

    //http://localhost:8080/book-car?carId=503&username=cust1
    @GetMapping("book-car")
    @ResponseBody
    public BookingDetailDao bookCar(@RequestParam int carId, @RequestParam String username, ModelMap modelMap) {
        Tent tent = tentRepository.findById(carId).get();
        tent.setAvailableForBooking(false);
        Booking newBooking=new Booking();
        newBooking.setCarId(carId);
        newBooking.setDriverId(tent.getDriverId());
        newBooking.setStatus("booked");
        newBooking.setUsername(username);
        tentRepository.save(tent);
        bookingRepository.save(newBooking);
        String cancelCarUrl="localhost:8080/cancel-tent?bookingId="+newBooking.getId();
        BookingDetailDao bookingDetailDao=new BookingDetailDao(newBooking,cancelCarUrl);
        return bookingDetailDao;
    }

    @RequestMapping("cancel-tent")
    public ResponseEntity<Object> cancelCar(@RequestParam int bookingId,ModelMap modelMap) {
        Optional<Booking> bookingOptional=bookingRepository.findById(bookingId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();
        // Booking doesn't exist or not the same user
        if(bookingOptional.isEmpty() || !bookingOptional.get().getUsername().equals((loggedInUsername))) {
            return new ResponseEntity<>("<h1>BAD REQUEST</h1>", HttpStatus.BAD_REQUEST);
        }

        Booking booking=bookingOptional.get();
        Tent tent = tentRepository.findById(booking.getCarId()).get();
        tent.setAvailableForBooking(true);
        tentRepository.save(tent);
        bookingRepository.deleteById(bookingId);
        return new ResponseEntity<>("<h1>Booking Canceled Successfully</h1>", HttpStatus.OK);
    }
}
