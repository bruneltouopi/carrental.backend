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
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MainApplicationClass.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class CustomerControllerTests {
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        TestHelper.loadSampleData(reservationRepository, customerRepository, carRepository);
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
        .andExpect(jsonPath("$[0].name", is("Lode Kennes")));
    }

    //endregion

    //region GetById

    @Test
    public void b_GetById() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/api/v1/customers/1").contentType(MediaType.APPLICATION_JSON);

        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.name", is("Lode Kennes")));
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
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/api/v1/customers/1/reservations").contentType(MediaType.APPLICATION_JSON);

        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
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
    public void e_PutCustomer() {

    }

    //endregion

    //region Delete

    @Test
    public void f_DeleteCustomer() throws Exception {
        mvc.perform(delete("/api/v1/customers/2"))
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
