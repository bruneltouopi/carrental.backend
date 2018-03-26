package com.lodekennes.carrental.backend;

import com.lodekennes.carrental.models.Customer;
import com.lodekennes.carrental.models.Reservation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerTests {
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void SetId() {
        Customer c = new Customer();
        c.setId(1);
        assertThat(c.getId()).isEqualTo(1);
    }

    @Test
    public void SetEmail() {
        Customer c = new Customer();
        c.setEmail("lode.kennes@accenture.com");
        assertThat(c.getEmail()).isEqualTo("lode.kennes@accenture.com");
    }

    @Test
    public void SetReservations() {
        Customer c = new Customer();
        c.setEmail("lode.kennes@accenture.com");

        Reservation r = new Reservation();
        Set<Reservation> reservations = new HashSet<Reservation>();
        reservations.add(r);

        c.setReservations(reservations);

        assertThat(c.getReservations().toArray()[0]).isEqualTo(r);
    }
}
