package com.lodekennes.carrental.controllers;

import com.lodekennes.carrental.exceptions.NotFoundException;
import com.lodekennes.carrental.models.Car;
import com.lodekennes.carrental.models.Customer;
import com.lodekennes.carrental.models.Reservation;
import com.lodekennes.carrental.repositories.CarRepository;
import com.lodekennes.carrental.repositories.CustomerRepository;
import com.lodekennes.carrental.repositories.ReservationRepository;
import com.lodekennes.carrental.services.DateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DateService dateService;

    @RequestMapping
    public Iterable<Reservation> get() {
        return reservationRepository.findAll();
    }

    @RequestMapping(value = "/{id}")
    public Reservation getById(@PathVariable int id) throws NotFoundException {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);

        if(!optionalReservation.isPresent())
            throw new NotFoundException("Reservation not found.");
        return optionalReservation.get();
    }

    @RequestMapping(value = "/{startDateString}/{endDateString}")
    public List<Reservation> getByDates(@PathVariable String startDateString, @PathVariable String endDateString) throws ParseException {
        Date startDate = dateService.parse(startDateString);
        Date endDate = dateService.parse(endDateString);

        List<Reservation> reservations = reservationRepository.findByDates(startDate, endDate);
        return reservations;
    }

    @PostMapping
    public Reservation post(@RequestBody @Valid Reservation reservation) throws NotFoundException {
        Customer customer = reservation.getCustomer();
        Car car = reservation.getCar();

        Optional<Customer> optionalCustomer = customerRepository.findById(customer.getId());
        reservation.setCustomer(optionalCustomer.get());

        Optional<Car> optionalCar = carRepository.findById(car.getId());
        car = optionalCar.get();

        long alreadyReservationsCount = car.getReservations().stream().filter(r -> dateService.isInBetween(r.getStartDate(), reservation.getStartDate(), reservation.getEndDate()) || dateService.isInBetween(r.getEndDate(), reservation.getStartDate(), reservation.getEndDate())).count();

        if(alreadyReservationsCount > 0)
            throw new NotFoundException("Car is not found for that period.");

        reservation.setCar(car);

        Reservation savedReservation = reservationRepository.save(reservation);
        return savedReservation;
    }
}
