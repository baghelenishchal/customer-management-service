package com.wrkspot.assignment.customer_management_service.service;

import com.wrkspot.assignment.customer_management_service.dto.AddressDTO;
import com.wrkspot.assignment.customer_management_service.dto.CustomerDTO;
import com.wrkspot.assignment.customer_management_service.entity.Address;
import com.wrkspot.assignment.customer_management_service.entity.Customer;
import com.wrkspot.assignment.customer_management_service.producer.KafkaProducer;
import com.wrkspot.assignment.customer_management_service.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    @Transactional
    public List<Customer> createCustomers(List<CustomerDTO> customerDTOs) {
        List<Customer> createdCustomers = new ArrayList<>();

        for (CustomerDTO customerDTO : customerDTOs) {
            Customer customer = new Customer();
            customer.setFirstName(customerDTO.getFirstName());
            customer.setLastName(customerDTO.getLastName());
            customer.setCustomerId(customerDTO.getCustomerId());
            customer.setAge(customerDTO.getAge());
            customer.setSpendingLimit(customerDTO.getSpendingLimit());
            customer.setMobileNumber(customerDTO.getMobileNumber());

            List<Address> addresses = new ArrayList<>();
            for (AddressDTO addressDTO : customerDTO.getAddress()) {
                Address address = new Address();
                address.setType(addressDTO.getType());
                address.setAddress1(addressDTO.getAddress1());
                address.setAddress2(addressDTO.getAddress2());
                address.setCity(addressDTO.getCity());
                address.setState(addressDTO.getState());
                address.setZipCode(addressDTO.getZipCode());
                addresses.add(address);
            }
            customer.setAddress(addresses);

            createdCustomers.add(customerRepository.save(customer));
            kafkaProducer.sendMessage(customer);
        }

        return createdCustomers;
    }

    @Override
    public List<Customer> getCustomers(String firstName, String city, String state) {
        Customer customer = new Customer();
        if (firstName != null && !firstName.isEmpty()) {
            customer.setFirstName(firstName);
        }

        List<Address> addresses = new ArrayList<>();
        Address address = new Address();
        if (city != null && !city.isEmpty()) {
            address.setCity(city);
        }
        if (state != null && !state.isEmpty()) {
            address.setState(state);
        }
        if (!addresses.isEmpty()) {
            addresses.add(address);
            customer.setAddress(addresses);
        }

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("address.city", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("address.state", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Customer> example = Example.of(customer, matcher);
        return customerRepository.findAll(example);
    }

    @Override
    public Map<String, List<Customer>> compareCustomerLists(List<Customer> listA, List<Customer> listB) {
        Set<Customer> setA = new HashSet<>(listA);
        Set<Customer> setB = new HashSet<>(listB);

        List<Customer> onlyInA = setA.stream()
                .filter(customer -> !setB.contains(customer))
                .collect(Collectors.toList());

        List<Customer> onlyInB = setB.stream()
                .filter(customer -> !setA.contains(customer))
                .collect(Collectors.toList());

        List<Customer> inBoth = setA.stream()
                .filter(setB::contains)
                .collect(Collectors.toList());

        Map<String, List<Customer>> result = new HashMap<>();
        result.put("onlyInA", onlyInA);
        result.put("onlyInB", onlyInB);
        result.put("inBoth", inBoth);

        return result;
    }
}