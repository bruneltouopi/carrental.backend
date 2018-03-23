package com.lodekennes.carrental.controllers;

import com.lodekennes.carrental.exceptions.NotFoundException;
import com.lodekennes.carrental.models.Customer;
import com.lodekennes.carrental.models.Reservation;
import com.lodekennes.carrental.repositories.CustomerRepository;
import com.lodekennes.carrental.services.CustomerService;
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
    private CustomerService customerService;

    @RequestMapping
    public Iterable<Customer> get() {
        return customerService.findAll();
    }

    @RequestMapping(value = "/{id}")
    public Customer get(@PathVariable int id) throws NotFoundException {
        return customerService.findById(id);
    }

    @RequestMapping(value = "/{id}/reservations")
    public Set<Reservation> getReservations(@PathVariable int id) throws NotFoundException {
        return customerService.findById(id).getReservations();
    }

    @PostMapping
    public Customer post(@RequestBody @Valid Customer customer) throws Exception {
        return customerService.save(customer);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Customer put(@PathVariable int id, @RequestBody Customer customer) throws InvalidKeySpecException, NoSuchAlgorithmException, NotFoundException {
        Customer realCustomer = customerService.findById(id);

        if(1 <= customer.getName().length())
            realCustomer.setName(customer.getName());
        if(1 <= customer.getEmail().length())
            realCustomer.setEmail(customer.getEmail().toLowerCase());

        Customer savedCustomer = customerService.save(realCustomer);
        return savedCustomer;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws NotFoundException {
        customerService.delete(id);
    }
}
