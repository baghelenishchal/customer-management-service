package com.wrkspot.assignment.customer_management_service;

import com.wrkspot.assignment.customer_management_service.dto.AddressDTO;
import com.wrkspot.assignment.customer_management_service.dto.CustomerDTO;
import com.wrkspot.assignment.customer_management_service.entity.Address;
import com.wrkspot.assignment.customer_management_service.entity.Customer;
import com.wrkspot.assignment.customer_management_service.producer.KafkaProducer;
import com.wrkspot.assignment.customer_management_service.repository.CustomerRepository;
import com.wrkspot.assignment.customer_management_service.service.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CustomerServiceTest {

	@InjectMocks
	private CustomerServiceImpl customerService;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private KafkaProducer kafkaProducer;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateCustomers() {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setFirstName("John");
		customerDTO.setLastName("Doe");
		customerDTO.setCustomerId("12345");
		customerDTO.setAge(30);
		customerDTO.setSpendingLimit(new BigDecimal("450000.001244"));
		customerDTO.setMobileNumber("1234567890");

		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setType("Home");
		addressDTO.setAddress1("123 Street");
		addressDTO.setAddress2("Apt 4");
		addressDTO.setCity("City");
		addressDTO.setState("State");
		addressDTO.setZipCode("12345");

		List<AddressDTO> addressDTOList = new ArrayList<>();
		addressDTOList.add(addressDTO);
		customerDTO.setAddress(addressDTOList);

		List<CustomerDTO> customerDTOs = new ArrayList<>();
		customerDTOs.add(customerDTO);

		Customer customer = new Customer();
		customer.setFirstName("John");
		customer.setLastName("Doe");
		customer.setCustomerId("12345");
		customer.setAge(30);
		customer.setSpendingLimit(new BigDecimal("450000.001244"));
		customer.setMobileNumber("1234567890");

		Address address = new Address();
		address.setType("Home");
		address.setAddress1("123 Street");
		address.setAddress2("Apt 4");
		address.setCity("City");
		address.setState("State");
		address.setZipCode("12345");

		List<Address> addressList = new ArrayList<>();
		addressList.add(address);
		customer.setAddress(addressList);

		when(customerRepository.save(any(Customer.class))).thenReturn(customer);

		List<Customer> createdCustomers = customerService.createCustomers(customerDTOs);

		assertEquals(1, createdCustomers.size());
		assertEquals("John", createdCustomers.get(0).getFirstName());
		assertEquals("Doe", createdCustomers.get(0).getLastName());
		assertEquals(new BigDecimal("450000.001244"), createdCustomers.get(0).getSpendingLimit());
		verify(customerRepository, times(1)).save(any(Customer.class));
		verify(kafkaProducer, times(1)).sendMessage(any(Customer.class));
	}

	@Test
	public void testGetCustomers() {
		Customer customer = new Customer();
		customer.setFirstName("John");
		customer.setLastName("Doe");
		customer.setCustomerId("12345");
		customer.setAge(30);
		customer.setSpendingLimit(new BigDecimal("450000.001244"));
		customer.setMobileNumber("1234567890");

		Address address = new Address();
		address.setType("Home");
		address.setAddress1("123 Street");
		address.setAddress2("Apt 4");
		address.setCity("City");
		address.setState("State");
		address.setZipCode("12345");

		List<Address> addressList = new ArrayList<>();
		addressList.add(address);
		customer.setAddress(addressList);

		List<Customer> customers = List.of(customer);

		ExampleMatcher matcher = ExampleMatcher.matching()
				.withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("address.city", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("address.state", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

		when(customerRepository.findAll(any(Example.class))).thenReturn(customers);

		List<Customer> foundCustomers = customerService.getCustomers("John", "City", "State");

		assertEquals(1, foundCustomers.size());
		assertEquals("John", foundCustomers.get(0).getFirstName());
		verify(customerRepository, times(1)).findAll(any(Example.class));
	}
	@Test
	public void testCompareCustomerLists() {
		Customer customer1 = new Customer();
		customer1.setFirstName("John");
		customer1.setLastName("Doe");
		customer1.setCustomerId("12345");
		customer1.setAge(30);
		customer1.setSpendingLimit(new BigDecimal("450000.001244"));
		customer1.setMobileNumber("1234567890");

		Customer customer2 = new Customer();
		customer2.setFirstName("Jane");
		customer2.setLastName("Doe");
		customer2.setCustomerId("67890");
		customer2.setAge(25);
		customer2.setSpendingLimit(new BigDecimal("250000.001244"));
		customer2.setMobileNumber("0987654321");

		Customer customer3 = new Customer();
		customer3.setFirstName("Alice");
		customer3.setLastName("Smith");
		customer3.setCustomerId("11111");
		customer3.setAge(28);
		customer3.setSpendingLimit(new BigDecimal("150000.001244"));
		customer3.setMobileNumber("5555555555");

		List<Customer> listA = List.of(customer1, customer2);
		List<Customer> listB = List.of(customer2, customer3);

		Map<String, List<Customer>> result = customerService.compareCustomerLists(listA, listB);

		assertEquals(1, result.get("onlyInA").size());
		assertEquals(customer1, result.get("onlyInA").get(0));

		assertEquals(1, result.get("onlyInB").size());
		assertEquals(customer3, result.get("onlyInB").get(0));

		assertEquals(1, result.get("inBoth").size());
		assertEquals(customer2, result.get("inBoth").get(0));
	}
}
