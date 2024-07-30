package com.wrkspot.assignment.customer_management_service.controller;

import com.wrkspot.assignment.customer_management_service.dto.CustomerDTO;
import com.wrkspot.assignment.customer_management_service.entity.Customer;
import com.wrkspot.assignment.customer_management_service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<List<Customer>> createCustomers(@RequestBody @Validated List<CustomerDTO> customerDTOs) {
        List<Customer> createdCustomers = customerService.createCustomers(customerDTOs);
        return ResponseEntity.ok(createdCustomers);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state) {
        List<Customer> customers = customerService.getCustomers(firstName, city, state);
        return ResponseEntity.ok(customers);
    }

    @PostMapping("/compare")
    public ResponseEntity<Map<String, List<Customer>>> compareCustomerLists(
            @RequestBody Map<String, List<Customer>> customerLists) {
        List<Customer> listA = customerLists.get("listA");
        List<Customer> listB = customerLists.get("listB");

        Map<String, List<Customer>> result = customerService.compareCustomerLists(listA, listB);
        return ResponseEntity.ok(result);
    }
}
