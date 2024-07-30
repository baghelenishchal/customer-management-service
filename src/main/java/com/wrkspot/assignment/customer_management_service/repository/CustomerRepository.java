package com.wrkspot.assignment.customer_management_service.repository;

import com.wrkspot.assignment.customer_management_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
