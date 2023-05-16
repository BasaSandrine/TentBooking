package com.assignment.tentservice.controller;

import com.assignment.tentservice.model.Tent;
import com.assignment.tentservice.model.TentRequest;
import com.assignment.tentservice.model.Customer;
import com.assignment.tentservice.repository.TentRepository;
import com.assignment.tentservice.repository.TentRequestRepository;
import com.assignment.tentservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TentRequestController {

    @Autowired
    TentRepository tentRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    TentRequestRepository tentRequestRepository;

    @RequestMapping("approve-request")
    public String approveDriverRequest(@RequestParam int requestId,
                                       @RequestParam int driverId,
                                       @RequestParam int carId) throws Exception {
        Customer customer = customerRepository.findById(driverId).orElseThrow(() ->
                new Exception("Customer not found with driverID - " + driverId));
        customer.setAssignedCarId(carId);
        customer.setUsedCarIds(customer.getUsedCarIds()+","+carId);
        Tent tent = tentRepository.findById(carId).orElseThrow(() ->
                new Exception("Car not found with carID - " + carId));

        TentRequest tentRequest = tentRequestRepository.findById(requestId).orElseThrow(() ->
                new Exception("Request not found with requestID - " + requestId));
        tentRequest.setRequestStatus("APPROVED");
        tent.setDriverId(driverId);
        tentRequestRepository.save(tentRequest);
        tentRepository.save(tent);
        customerRepository.save(customer);
        return "redirect:/list-car-requests";
    }

    @RequestMapping("reject-request")
    public String rejectDriverRequest(@RequestParam int requestId) throws Exception {
        TentRequest tentRequest = tentRequestRepository.findById(requestId).orElseThrow(() ->
                new Exception("Request not found with requestID - " + requestId));
        tentRequest.setRequestStatus("REJECTED");
        tentRequestRepository.save(tentRequest);
        return "redirect:/list-tent-requests";
    }

    @RequestMapping("delete-tent-requests")
    public String rejectDriverRequest()  {
        tentRequestRepository.deleteAll();
        return "redirect:/list-tent-requests";
    }

    //localhost:8080/list-tent-requests/
    @RequestMapping("list-tent-requests")
    public String listAllTentRequests(ModelMap modelMap) {
        List<TentRequest> tentRequests = tentRequestRepository.findAll();
        modelMap.put("tent_requests", tentRequests);
        return "listTentRequests";
    }

}
