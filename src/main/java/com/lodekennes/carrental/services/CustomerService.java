package com.lodekennes.carrental.services;

import com.lodekennes.carrental.exceptions.NotFoundException;
import com.lodekennes.carrental.models.Customer;
import com.lodekennes.carrental.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public final Iterable<Customer> findAll() {
        return customerRepository.findAll();
    }

    public final long countAll() {
        return customerRepository.count();
    }

    public final Customer findById(int id) throws NotFoundException {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if(!optionalCustomer.isPresent())
            throw new NotFoundException("Customer not found.");

        return optionalCustomer.get();
    }

    public final Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public final void delete(int id) throws NotFoundException {
        try {
            customerRepository.deleteById(id);
        } catch(EmptyResultDataAccessException e) {
            throw new NotFoundException("Customer not found.");
        }
    }
}
