package com.assignment.tentservice.controller;

import com.assignment.tentservice.exception.InvalidSeatingCapacityException;
import com.assignment.tentservice.model.Customer;
import com.assignment.tentservice.model.Tent;
import com.assignment.tentservice.repository.TentRepository;
import com.assignment.tentservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TentController {

    @Autowired
    private TentRepository tentRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping("list-cars")
    public String listAllCars(ModelMap modelMap) {
        List<Tent> tents = tentRepository.findAll();
        modelMap.put("cars", tents);
        return "listCars";
    }

    @RequestMapping("list-available-cars")
    public String listAllAvailableCarsForBooking(@RequestParam int seatingCapacity,ModelMap modelMap) {
        List<Tent> tents = tentRepository.findBySeatingCapacityAndAvailableForBookingTrue(seatingCapacity);
        modelMap.put("cars", tents);
        return "listCarsAvailableForBooking";
    }

    @RequestMapping(value="add-car",method= RequestMethod.GET)
    public String showNewCarPage(Tent tent) {
        return "car";
    }

    //public String addNewTodo(@Valid Todo todo, ModelMap modelMap, BindingResult bindingResult) {
    @RequestMapping(value="add-car",method= RequestMethod.POST)
    public String addNewCar(Tent tent) throws Exception {
        int capacity= tent.getSeatingCapacity();
        if(capacity!=3 && capacity!=4 && capacity!=7) {
            throw  new InvalidSeatingCapacityException("Allowed capacities are: {3,4,7}");
        }

        tent.setAvailableForBooking(true);
        tentRepository.save(tent);
        return "redirect:list-cars";
    }

    //http://localhost:8080/delete-car?id=502
    @RequestMapping(value="delete-car")
    public String deleteCar(@RequestParam int id) {
        tentRepository.deleteById(id);
        return "redirect:list-cars";
    }



    //localhost:8080/assign-car/carId/503/driverId/152
    @GetMapping(value="assign-car/carId/{carId}/driverId/{driverId}")
    public String assignDriverToCar(@PathVariable int carId,@PathVariable int driverId) throws Exception {
        Customer customer = customerRepository.findById(driverId).orElseThrow(() ->
                new Exception("Customer not found with driverID - " + driverId));
        int previousAssignedCarId= customer.getAssignedCarId();
        customer.setAssignedCarId(carId);
        customer.setUsedCarIds(customer.getUsedCarIds()+","+carId);
        Tent previousAssignedTent = tentRepository.findById(previousAssignedCarId).orElseThrow(() ->
                new Exception("Car not found with carID - " + previousAssignedCarId));;
        previousAssignedTent.setDriverId(null);
        Tent tent = tentRepository.findById(carId).orElseThrow(() ->
                new Exception("Car not found with carID - " + carId));;
        tent.setDriverId(driverId);
        tentRepository.save(previousAssignedTent);
        tentRepository.save(tent);
        customerRepository.save(customer);
        return "redirect:/list-cars";
    }

    /*@GetMapping(path="/jpa/users/{id}/posts")
    public List<Post> retrievePostsForUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return user.get().getPosts();
    }*/
}
