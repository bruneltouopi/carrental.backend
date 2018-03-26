package com.lodekennes.carrental.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lodekennes.carrental.models.Car;
import com.lodekennes.carrental.models.Customer;
import com.lodekennes.carrental.models.Reservation;
import com.lodekennes.carrental.repositories.CarRepository;
import com.lodekennes.carrental.repositories.CustomerRepository;
import com.lodekennes.carrental.repositories.ReservationRepository;

import java.util.Date;

public class TestHelper {
    private static boolean loaded = false;

    public static void loadSampleData(ReservationRepository reservationRepository, CustomerRepository customerRepository, CarRepository carRepository) {
        if(loaded) return;

        //Sample Customers
        Customer customer1 = new Customer("Lode Kennes", "lode.kennes@accenture.com");
        customer1.setId(1);
        Customer customer2 = new Customer("Bram Van Asschodt", "bram.van.asschodt@accenture.com");
        customer2.setId(2);
        Customer customer3 = new Customer("Maxmine Walravens", "maxime.walravens@accenture.com");
        customer3.setId(3);
        Customer customer4 = new Customer("Ruben Van Den Abeele", "ruben.van.den.abeele@accenture.com");
        customer4.setId(4);

        customer1 = customerRepository.save(customer1);
        customer2 = customerRepository.save(customer2);
        customer3 = customerRepository.save(customer3);
        customer4 = customerRepository.save(customer4);

        //Sample Cars
        Car car1 = new Car("Toyota V1", 5);
        car1.setId(1);
        Car car2 = new Car("BMW E3", 9);
        car1.setId(2);
        Car car3 = new Car("Mercedes SLS", 15);
        car1.setId(3);
        Car car4 = new Car("Mini", 7);
        car1.setId(4);

        car1 = carRepository.save(car1);
        car2 = carRepository.save(car2);
        car3 = carRepository.save(car3);
        car4 = carRepository.save(car4);

        //Sample Reservations
        Reservation reservation1 = new Reservation(new Date(), new Date(), customer1, car1, false);

        reservationRepository.save(reservation1);
        loaded = true;
    }

    public static String jsonSerialize(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(obj);
    }
}
