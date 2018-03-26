package com.lodekennes.carrental.backend;

import com.lodekennes.carrental.MainApplicationClass;
import com.lodekennes.carrental.models.Customer;
import com.lodekennes.carrental.repositories.CarRepository;
import com.lodekennes.carrental.repositories.CustomerRepository;
import com.lodekennes.carrental.repositories.ReservationRepository;
import com.lodekennes.carrental.services.CustomerService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MainApplicationClass.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class CustomerControllerTests {
    private TestHelper testHelper;

    @Before
    public void init() {
        testHelper = TestHelper.loadSampleData(reservationRepository, customerRepository, carRepository);
    }

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MockMvc mvc;

    //region Get

    @Test
    public void a_Get() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/api/v1/customers").contentType(MediaType.APPLICATION_JSON);

        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
        .andExpect(content()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(testHelper.customers.size())));
    }

    //endregion

    //region GetById

    @Test
    public void b_GetById() throws Exception {
        Customer expectedCustomer = testHelper.getCustomer(0);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/api/v1/customers/" + expectedCustomer.getId()).contentType(MediaType.APPLICATION_JSON);

        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.name", is(expectedCustomer.getName())));
    }

    @Test
    public void b_GetById_Bad() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/api/v1/customers/99").contentType(MediaType.APPLICATION_JSON);

        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().is(404));
    }

    //endregion

    //region GetReservationsById

    @Test
    public void c_GetReservationsById() throws Exception {
        Customer customer = testHelper.getCustomer(0);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/api/v1/customers/" + customer.getId() + "/reservations").contentType(MediaType.APPLICATION_JSON);

        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(customer.getReservations().size())));
    }

    //endregion

    //region Post

    @Test
    public void d_PostCustomer() throws Exception {
        Customer michiel = new Customer("Michiel Vanhaverbeke", "michiel.vanhaverbeke@accenture.com");

        String serialized = TestHelper.jsonSerialize(michiel);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialized);

        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Michiel Vanhaverbeke")))
                .andExpect(jsonPath("$.email", is("michiel.vanhaverbeke@accenture.com")))
                .andExpect(jsonPath("$.id", greaterThan(0)));
    }

    @Test
    public void d_PostCustomer_Bad_NoName() throws Exception {
        Customer michiel = new Customer("", "michiel.vanhaverbeke@accenture.com");

        String serialized = TestHelper.jsonSerialize(michiel);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialized);

        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    public void d_PostCustomer_Bad_NoEmail() throws Exception {
        Customer michiel = new Customer("Michiel Vanhaverbeke", "");

        String serialized = TestHelper.jsonSerialize(michiel);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialized);

        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    public void d_PostCustomer_Bad_BadEmail() throws Exception {
        Customer michiel = new Customer("Michiel Vanhaverbeke", "thisisnotanemail");

        String serialized = TestHelper.jsonSerialize(michiel);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialized);

        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().is(400));
    }

    //endregion

    //region Put

    @Test
    public void e_PutCustomer() throws Exception {
        Customer customerThatWillBeModified = testHelper.getCustomer(1);
        Customer customerToModify = new Customer("Griet Coysmans", "griet.coysmans@accenture.com");
        String serialized = TestHelper.jsonSerialize(customerToModify);

        mvc.perform(
          put("/api/v1/customers/" + customerThatWillBeModified.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialized)
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("Griet Coysmans")))
        .andExpect(jsonPath("$.email", is("griet.coysmans@accenture.com")))
        .andDo(print());
    }

    @Test
    public void e_PutCustomer_OnlyName() throws Exception {
        Customer customerThatWillBeModified = testHelper.getCustomer(1);
        Customer customerToModify = new Customer("Griet Coysmans", "");
        String serialized = TestHelper.jsonSerialize(customerToModify);

        mvc.perform(
                put("/api/v1/customers/" + customerThatWillBeModified.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serialized)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Griet Coysmans")))
                .andExpect(jsonPath("$.email", is(customerThatWillBeModified.getEmail())))
                .andDo(print());
    }

    @Test
    public void e_PutCustomer_OnlyEmail() throws Exception {
        Customer customerThatWillBeModified = testHelper.getCustomer(2);
        Customer customerToModify = new Customer("", "griet.coysmans@accenture.com");
        String serialized = TestHelper.jsonSerialize(customerToModify);

        mvc.perform(
                put("/api/v1/customers/" + customerThatWillBeModified.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serialized)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(customerThatWillBeModified.getName())))
                .andExpect(jsonPath("$.email", is("griet.coysmans@accenture.com")))
                .andDo(print());
    }

    @Test
    public void e_PutCustomer_Bad_InvalidId() throws Exception {
        Customer customerToModify = new Customer("Lode Kennes", "lode.kennes@accenture.com");
        String serialized = TestHelper.jsonSerialize(customerToModify);

        mvc.perform(
                put("/api/v1/customers/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serialized)
        ).andExpect(status().is(404));
    }

    //endregion

    //region Delete

    @Test
    public void f_DeleteCustomer() throws Exception {
        Customer customerThatWillBeDeleted = testHelper.getCustomer(3);
        mvc.perform(delete("/api/v1/customers/" + customerThatWillBeDeleted.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void f_DeleteCustomer_InvalidId() throws Exception {
        mvc.perform(delete("/api/v1/customers/10000"))
                .andExpect(status().is(404));
    }

    @Test
    public void f_DeleteCustomer_NoId() throws Exception {
        mvc.perform(delete("/api/v1/customers"))
                .andExpect(status().isOk());
    }

    //endregion
}
