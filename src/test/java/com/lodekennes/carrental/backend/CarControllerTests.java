package com.lodekennes.carrental.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lodekennes.carrental.MainApplicationClass;
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
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;

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
public class CarControllerTests {
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
    public void get_Get() throws Exception {
        Car expectedCar = testHelper.getCar(0);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/api/v1/cars").contentType(MediaType.APPLICATION_JSON);

        mvc.perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is(expectedCar.getName())));
    }

    //endregion

    //region GetById

    @Test
    public void getById_GetById() throws Exception {
        Car expectedCar = testHelper.getCar(0);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/api/v1/cars/" + expectedCar.getId()).contentType(MediaType.APPLICATION_JSON);

        mvc.perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(expectedCar.getName())));
    }

    @Test
    public void getById_GetById_Bad() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/api/v1/cars/9999999" ).contentType(MediaType.APPLICATION_JSON);

        mvc.perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpect(status().is(404));
    }

    //endregion

    //region GetReservationsById

    @Test
    public void reservations_GetReservationsById() throws Exception {
        Car car = testHelper.getCar(0);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/api/v1/cars/" + car.getId() + "/reservations").contentType(MediaType.APPLICATION_JSON);

        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(car.getReservations().size())));
    }

    //endregion

    //region GetAvailable
    @Test
    public void available_GetAvailable_OneReservation() throws Exception {
        Reservation reservation = testHelper.getReservation(0);
        mvc.perform(get("/api/v1/cars/" + dateService.formatDate(reservation.getStartDate()) + "/" + dateService.formatDate(reservation.getEndDate())))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(testHelper.cars.size() - 1)));
    }

    @Test
    public void available_GetAvailable_All() throws Exception {
        Reservation reservation = testHelper.getReservation(0);
        mvc.perform(get("/api/v1/cars/1970-01-01/1970-01-01"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(testHelper.cars.size())));
    }

    @Test(expected = Exception.class)
    public void available_GetAvailable_Bad_StartDate() throws Exception {
        mvc.perform(get("/api/v1/cars/1970-01-x/1970-01-01"));
    }

    @Test(expected = Exception.class)
    public void available_GetAvailable_Bad_EndDate() throws Exception {
        mvc.perform(get("/api/v1/cars/1970-01-01/1970-01-x"));
    }
    //endregion

    //region Post
    @Test
    public void post_AddCar() throws Exception {
        Car c = new Car("Hyundai i8", 99);
        String seralized = TestHelper.jsonSerialize(c);
        mvc.perform(post("/api/v1/cars")
        .contentType(MediaType.APPLICATION_JSON)
        .content(seralized))
                .andExpect(status().isOk())
        .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is("Hyundai i8")))
        .andExpect(jsonPath("$.pricePerDay", is(99.0)));
    }

    @Test
    public void post_AddCar_Bad_LowPrice() throws Exception {
        Car c = new Car("Hyundai i8", -1);
        String seralized = TestHelper.jsonSerialize(c);
        mvc.perform(post("/api/v1/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(seralized))
                .andExpect(status().is(400));
    }

    @Test
    public void post_AddCar_Bad_NoName() throws Exception {
        Car c = new Car("", 1);
        String seralized = TestHelper.jsonSerialize(c);
        mvc.perform(post("/api/v1/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(seralized))
                .andExpect(status().is(400));
    }
    //endregion

    //region Put
    @Test
    public void put_Put() throws Exception {
        Car carToModify = testHelper.getCar(1);
        carToModify.setName("Hello");
        carToModify.setPricePerDay(9);

        String serialized = TestHelper.jsonSerialize(carToModify);

        mvc.perform(put("/api/v1/cars/" + carToModify.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(serialized))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(carToModify.getId())))
                .andExpect(jsonPath("$.name", is("Hello")))
                .andExpect(jsonPath("$.pricePerDay", is(9.0)));

    }
    //endregion

    //region Delete

    @Test
    public void delete_DeleteCar() throws Exception {
        Car carThatWillBeDeleted = testHelper.getCar(3);
        mvc.perform(delete("/api/v1/cars/" + carThatWillBeDeleted.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void delete_DeleteCar_InvalidId() throws Exception {
        mvc.perform(delete("/api/v1/cars/10000"))
                .andExpect(status().is(404));
    }

    @Test
    public void delete_DeleteCar_NoId() throws Exception {
        mvc.perform(delete("/api/v1/cars"))
                .andExpect(status().isOk());
    }

    //endregion
}
