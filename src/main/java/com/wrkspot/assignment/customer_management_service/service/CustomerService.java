package com.wrkspot.assignment.customer_management_service.service;


import com.wrkspot.assignment.customer_management_service.dto.CustomerDTO;
import com.wrkspot.assignment.customer_management_service.entity.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    List<Customer> createCustomers(List<CustomerDTO> customerDTOs);
    List<Customer> getCustomers(String firstName, String city, String state);
    Map<String, List<Customer>> compareCustomerLists(List<Customer> listA, List<Customer> listB);
}
