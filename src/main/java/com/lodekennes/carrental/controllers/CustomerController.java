package com.lodekennes.carrental.controllers;

import com.lodekennes.carrental.exceptions.NotFoundException;
import com.lodekennes.carrental.models.Customer;
import com.lodekennes.carrental.models.Reservation;
import com.lodekennes.carrental.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping
    public Iterable<Customer> get() {
        Iterable<Customer> customers = customerRepository.findAll();
        return customers;
    }

    @RequestMapping(value = "/{id}")
    public Customer get(@PathVariable int id) throws NotFoundException {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if(!optionalCustomer.isPresent())
            throw new NotFoundException("Customer not found.");

        return optionalCustomer.get();
    }

    @RequestMapping(value = "/{id}/reservations")
    public Set<Reservation> getReservations(@PathVariable int id) throws NotFoundException {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if(!optionalCustomer.isPresent())
            throw new NotFoundException("Customer not found.");

        return optionalCustomer.get().getReservations();
    }

    @PostMapping
    public Customer post(@RequestBody @Valid Customer customer) throws Exception {
        return customerRepository.save(customer);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Customer put(@PathVariable int id, @RequestBody Customer customer) throws InvalidKeySpecException, NoSuchAlgorithmException, NotFoundException {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if(!optionalCustomer.isPresent())
            throw new NotFoundException("Customer not found.");

        Customer realCustomer = optionalCustomer.get();

        if(1 <= customer.getName().length())
            realCustomer.setName(customer.getName());
        if(1 <= customer.getEmail().length())
            realCustomer.setEmail(customer.getEmail().toLowerCase());

        Customer savedCustomer = customerRepository.save(realCustomer);
        return savedCustomer;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws NotFoundException {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if(!optionalCustomer.isPresent())
            throw new NotFoundException("Customer not found.");

        customerRepository.delete(optionalCustomer.get());
    }
}
