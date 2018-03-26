package com.lodekennes.carrental.backend;

import com.lodekennes.carrental.exceptions.NotFoundException;
import com.lodekennes.carrental.models.Customer;
import com.lodekennes.carrental.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositoryTests {
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        Customer customer1 = new Customer("Lode Kennes", "lode.kennes@accenture.com");
        Customer customer2 = new Customer("Bram Van Asschodt", "bram.van.asschodt@accenture.com");
        Customer customer3 = new Customer("Maxmine Walravens", "maxime.walravens@accenture.com");
        Customer customer4 = new Customer("Ruben Van Den Abeele", "ruben.van.den.abeele@accenture.com");

        entityManager.persist(customer1);
        entityManager.persist(customer2);
        entityManager.persist(customer3);
        entityManager.persist(customer4);
        entityManager.flush();
    }

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;


    @Test
    public void defaultAmount4() {
        long count = customerRepository.count();
        assertThat(count).isEqualTo(4L);
    }

    @Test
    public void findCustomer() throws NotFoundException {
        Iterable<Customer> found = customerRepository.findAll();
        Customer first = found.iterator().next();
        assertThat(first.getName()).isEqualTo("Lode Kennes");
    }

    @Test
    public void deleteCustomer() throws NotFoundException {
        customerRepository.deleteById(2);
        long count = customerRepository.count();
        assertThat(count).isEqualTo(3L);
    }
}
