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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TestHelper {
    //region non-static
    public ArrayList<Customer> customers;
    public ArrayList<Car> cars;
    public ArrayList<Reservation> reservations;

    public TestHelper(ArrayList<Customer> customerList, ArrayList<Car> carList, ArrayList<Reservation> reservationList) {
        this.customers = customerList;
        this.cars = carList;
        this.reservations = reservationList;
    }

    public Customer getCustomer(int index) {
        return this.customers.get(index);
    }

    public Car getCar(int index) {
        return this.cars.get(index);
    }

    public Reservation getReservation(int index) {
        return this.reservations.get(index);
    }

    //endregion

    //region static

    public static TestHelper loadSampleData(ReservationRepository reservationRepository, CustomerRepository customerRepository, CarRepository carRepository) {

        //Sample Customers
        Customer customer1 = new Customer("Lode Kennes", "lode.kennes@accenture.com");
        Customer customer2 = new Customer("Bram Van Asschodt", "bram.van.asschodt@accenture.com");
        Customer customer3 = new Customer("Maxmine Walravens", "maxime.walravens@accenture.com");
        Customer customer4 = new Customer("Ruben Van Den Abeele", "ruben.van.den.abeele@accenture.com");
        Customer customer5 = new Customer("Arne Van Bael", "arne.van.bael@accenture.com");

        customer1 = customerRepository.save(customer1);
        customer2 = customerRepository.save(customer2);
        customer3 = customerRepository.save(customer3);
        customer4 = customerRepository.save(customer4);
        customer5 = customerRepository.save(customer5);

        ArrayList<Customer> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);
        customers.add(customer4);
        customers.add(customer5);


        //Sample Cars
        Car car1 = new Car("Toyota V1", 5);
        Car car2 = new Car("BMW E3", 9);
        Car car3 = new Car("Mercedes SLS", 15);
        Car car4 = new Car("Mini", 7);

        car1 = carRepository.save(car1);
        car2 = carRepository.save(car2);
        car3 = carRepository.save(car3);
        car4 = carRepository.save(car4);

        ArrayList<Car> cars = new ArrayList<>();
        cars.add(car1);
        cars.add(car2);
        cars.add(car3);
        cars.add(car4);

        //Sample Reservations
        Reservation reservation1 = new Reservation(new Date(), new Date(), customer1, car1, false);

        reservationRepository.save(reservation1);

        //Add reservations to related objs
        Set<Reservation> reservationsSet = new HashSet<>();
        reservationsSet.add(reservation1);

        customer1.setReservations(reservationsSet);
        car1.setReservations(reservationsSet);

        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation1);

        //Returning the dataset
        return new TestHelper(customers, cars, reservations);
    }

    public static String jsonSerialize(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(obj);
    }
    //endregion
}
