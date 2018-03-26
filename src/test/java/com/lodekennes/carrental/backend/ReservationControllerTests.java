package com.lodekennes.carrental.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lodekennes.carrental.MainApplicationClass;
import com.lodekennes.carrental.exposedmodels.ExposedReservation;
import com.lodekennes.carrental.models.Car;
import com.lodekennes.carrental.models.Customer;
import com.lodekennes.carrental.models.Reservation;
import com.lodekennes.carrental.repositories.CarRepository;
import com.lodekennes.carrental.repositories.CustomerRepository;
import com.lodekennes.carrental.repositories.ReservationRepository;
import com.lodekennes.carrental.services.CustomerService;
import com.lodekennes.carrental.services.DateService;
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

import java.util.Date;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MainApplicationClass.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class ReservationControllerTests {
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
    private DateService dateService;

    @Autowired
    private MockMvc mvc;

    //region Get
    @Test
    public void a_Get() throws Exception {
        Reservation expectedReservation = testHelper.getReservation(0);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/api/v1/reservations").contentType(MediaType.APPLICATION_JSON);

        mvc.perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(expectedReservation.getId())));
    }
    //endregion

    //region GetById

    @Test
    public void b_GetById() throws Exception {
        Reservation expectedReservation = testHelper.getReservation(0);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/api/v1/reservations/" + expectedReservation.getId()).contentType(MediaType.APPLICATION_JSON);

        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.id", is(expectedReservation.getId())));
    }

    @Test
    public void b_GetById_Bad() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/api/v1/reservations/99").contentType(MediaType.APPLICATION_JSON);

        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().is(404));
    }

    //endregion

    //region GetByDates
    @Test
    public void c_GetByDates() throws Exception {
        Reservation reservation = testHelper.getReservation(0);
        mvc.perform(get("/api/v1/reservations/" + dateService.formatDate(reservation.getStartDate()) + "/" + dateService.formatDate(reservation.getEndDate())))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void c_GetByDates_Empty() throws Exception {
        Reservation reservation = testHelper.getReservation(0);
        mvc.perform(get("/api/v1/reservations/1970-03-01/1970-03-15"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test(expected = Exception.class)
    public void c_GetByDates_Bad_StartDate() throws Exception {
        mvc.perform(get("/api/v1/reservations/1970-01-x/1970-01-01"));
    }

    @Test(expected = Exception.class)
    public void c_GetByDates_Bad_EndDate() throws Exception {
        mvc.perform(get("/api/v1/reservations/1970-01-01/1970-01-x"));
    }
    //endregion

    //region Post
    @Test
    public void d_Post() throws Exception {
        Car carToUse = testHelper.getCar(1);
        Customer customerToUse = testHelper.getCustomer(1);
        Date startDate = new Date();
        Date endDate = new Date();

        Reservation newReservation = new Reservation(startDate, endDate, customerToUse, carToUse, false);

        String serialized = TestHelper.jsonSerialize(newReservation);

        mvc.perform(post("/api/v1/reservations")
        .contentType(MediaType.APPLICATION_JSON)
        .content(serialized))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", greaterThan(0)));
    }

    @Test
    public void d_Post_Bad_CarUnavailable_Exact() throws Exception {
        Car carToUse = testHelper.getCar(0);
        Customer customerToUse = testHelper.getCustomer(1);

        Reservation newReservation = new Reservation(new Date(2018, 12, 3), new Date(2018, 12, 5), customerToUse, carToUse, false);

        String serialized = TestHelper.jsonSerialize(newReservation);

        mvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialized))
                .andExpect(status().is(404));
    }

    @Test
    public void d_Post_Bad_CarUnavailable_BeforeOverlap() throws Exception {
        Car carToUse = testHelper.getCar(0);
        Customer customerToUse = testHelper.getCustomer(1);

        Reservation newReservation = new Reservation(new Date(2018, 12, 1), new Date(2018, 12, 5), customerToUse, carToUse, false);

        String serialized = TestHelper.jsonSerialize(newReservation);

        mvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialized))
                .andExpect(status().is(404));
    }

    @Test
    public void d_Post_Bad_CarUnavailable_AfterOverlap() throws Exception {
        Car carToUse = testHelper.getCar(0);
        Customer customerToUse = testHelper.getCustomer(1);

        Reservation newReservation = new Reservation(new Date(2018, 12, 3), new Date(2018, 12, 9), customerToUse, carToUse, false);

        String serialized = TestHelper.jsonSerialize(newReservation);

        mvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialized))
                .andExpect(status().is(404));
    }

    @Test
    public void d_Post_Bad_CarUnavailable_FullOverlap() throws Exception {
        Car carToUse = testHelper.getCar(0);
        Customer customerToUse = testHelper.getCustomer(1);

        Reservation newReservation = new Reservation(new Date(2018, 12, 1), new Date(2018, 12, 15), customerToUse, carToUse, false);

        String serialized = TestHelper.jsonSerialize(newReservation);

        mvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialized))
                .andExpect(status().is(404));
    }

    @Test
    public void d_Post_OtherCar_Exact() throws Exception {
        Car carToUse = testHelper.getCar(1);
        Customer customerToUse = testHelper.getCustomer(1);

        Reservation newReservation = new Reservation(new Date(2018, 12, 3), new Date(2018, 12, 5), customerToUse, carToUse, false);

        String serialized = TestHelper.jsonSerialize(newReservation);

        mvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialized))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", greaterThan(0)));
    }

    @Test
    public void d_Post_OtherCar_BeforeOverlap() throws Exception {
        Car carToUse = testHelper.getCar(1);
        Customer customerToUse = testHelper.getCustomer(1);

        Reservation newReservation = new Reservation(new Date(2018, 12, 1), new Date(2018, 12, 5), customerToUse, carToUse, false);

        String serialized = TestHelper.jsonSerialize(newReservation);

        mvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialized))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", greaterThan(0)));
    }

    @Test
    public void d_Post_OtherCar_AfterOverlap() throws Exception {
        Car carToUse = testHelper.getCar(1);
        Customer customerToUse = testHelper.getCustomer(1);

        Reservation newReservation = new Reservation(new Date(2018, 12, 3), new Date(2018, 12, 9), customerToUse, carToUse, false);

        String serialized = TestHelper.jsonSerialize(newReservation);

        mvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialized))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", greaterThan(0)));
    }

    @Test
    public void d_Post_OtherCar_FullOverlap() throws Exception {
        Car carToUse = testHelper.getCar(1);
        Customer customerToUse = testHelper.getCustomer(1);

        Reservation newReservation = new Reservation(new Date(2018, 12, 1), new Date(2018, 12, 15), customerToUse, carToUse, false);

        String serialized = TestHelper.jsonSerialize(newReservation);

        mvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialized))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", greaterThan(0)));
    }

    @Test
    public void d_Post_Customer_BadId() throws Exception {
        ExposedReservation newReservation = new ExposedReservation(new Date(2018, 12, 1), new Date(2018, 12, 15), 99, 1, false);

        String serialized = TestHelper.jsonSerialize(newReservation);

        mvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialized))
                .andExpect(status().is(400));
    }

    @Test
    public void d_Post_Car_BadId() throws Exception {
        ExposedReservation newReservation = new ExposedReservation(new Date(2018, 12, 1), new Date(2018, 12, 15), 1, 99, false);

        String serialized = TestHelper.jsonSerialize(newReservation);

        mvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialized))
                .andExpect(status().is(400));
    }

    //endregion

    //region Delete
    @Test
    public void e_Delete() throws Exception {
        Reservation reservation = testHelper.getReservation(0);

        mvc.perform(delete("/api/v1/reservations/" + reservation.getId())
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void e_Delete_BadId() throws Exception {
        mvc.perform(delete("/api/v1/reservations/9999999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
    //endregion
}
