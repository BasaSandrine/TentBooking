package com.assignment.tentservice.controller;

import com.assignment.tentservice.dao.CustomerUseTentDao;
import com.assignment.tentservice.model.Customer;
import com.assignment.tentservice.model.Tent;
import com.assignment.tentservice.model.TentRequest;
import com.assignment.tentservice.repository.TentRepository;
import com.assignment.tentservice.repository.TentRequestRepository;
import com.assignment.tentservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TentRepository tentRepository;
    @Autowired
    private TentRequestRepository tentRequestRepository;

    @RequestMapping("list-drivers")
    public String listAllDrivers(ModelMap modelMap) {
        List<Customer> customers = customerRepository.findAll();
        modelMap.put("customers", customers);
        return "listDrivers";
    }

    @GetMapping("driver/used-tents")
    @ResponseBody
    public CustomerUseTentDao getCarsUsedByDriver(@RequestParam int driverId) throws Exception {
        Optional<Customer> driverOptional= customerRepository.findById(driverId);
        if(driverOptional.isPresent()) {
            Customer customer =driverOptional.get();
            String[] usedCars= customer.getUsedCarIds().split(",");
            List<Integer> carIds=new ArrayList<>();
            for(String cardId:usedCars) {
                carIds.add(Integer.parseInt(cardId));
            }

            List<Tent> tentList = tentRepository.findByIdIn(carIds);
            CustomerUseTentDao customerUseTentDao =new CustomerUseTentDao(driverId, customer.getUsername(), tentList);

            return customerUseTentDao;
        }

        throw new Exception("Customer not found");
    }

    @RequestMapping(value="add-customer",method= RequestMethod.GET)
    public String showNewDriverPage(Customer customer) {
        return "customer";
    }

    //public String addNewTodo(@Valid Todo todo, ModelMap modelMap, BindingResult bindingResult) {
    @RequestMapping(value="add-customer",method= RequestMethod.POST)
    public String addNewDriver(Customer customer) {
        customer.setPassword("$2a$12$TLJOLK.QjLRdxOHew1XMT.eYa2Xr5HMHaT14fRoI3gMOIZijNu9F2");//123
        customer.setUsedCarIds(""+ customer.getAssignedCarId());
        Customer savedCustomer = customerRepository.save(customer);
        Tent tent = tentRepository.findById(customer.getAssignedCarId()).get();
        tent.setDriverId(savedCustomer.getId());
        tentRepository.save(tent);
        return "redirect:list-drivers";
    }
    //http://localhost:8080/delete-driver?id=102
    @RequestMapping(value="delete-driver")
    public String deleteDriver(@RequestParam int id) throws Exception {
        Customer customer = customerRepository.findById(id).orElseThrow(() ->
                new Exception("Customer not found with driverID - " + id));
        Tent tent = tentRepository.findById(customer.getAssignedCarId()).orElseThrow(() ->
                new Exception("Car not found with carID - " + customer.getAssignedCarId()));
        tent.setAvailableForBooking(true);
        tent.setDriverId(null);
        tentRepository.save(tent);
        customerRepository.deleteById(id);
        return "redirect:list-drivers";
    }

    //http://localhost:8080/request-car?driverId=102&carId=402
    @GetMapping(value="request-car")
    public String requestNewCar(@RequestParam int driverId,@RequestParam int carId) {
        TentRequest newTentRequest =new TentRequest();
        newTentRequest.setDriverId(driverId);
        newTentRequest.setCarId(carId);
        newTentRequest.setRequestStatus("PENDING");
        tentRequestRepository.save(newTentRequest);
        return "redirect:list-car-requests";
    }

}
