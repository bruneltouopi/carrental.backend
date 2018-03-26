package com.lodekennes.carrental.repositories;

import com.lodekennes.carrental.models.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    Optional<Customer> findByEmail(String email);
}
