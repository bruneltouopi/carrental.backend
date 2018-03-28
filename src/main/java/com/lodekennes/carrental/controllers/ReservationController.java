package com.lodekennes.carrental.controllers;

import com.lodekennes.carrental.exceptions.NotFoundException;
import com.lodekennes.carrental.exposedmodels.ExposedReservation;
import com.lodekennes.carrental.models.Car;
import com.lodekennes.carrental.models.Customer;
import com.lodekennes.carrental.models.Reservation;
import com.lodekennes.carrental.repositories.CarRepository;
import com.lodekennes.carrental.repositories.CustomerRepository;
import com.lodekennes.carrental.repositories.ReservationRepository;
import com.lodekennes.carrental.services.CarService;
import com.lodekennes.carrental.services.CustomerService;
import com.lodekennes.carrental.services.DateService;
import com.lodekennes.carrental.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private CarService carService;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private DateService dateService;

    @RequestMapping
    public Iterable<Reservation> get() {
        return reservationService.findAll();
    }

    @RequestMapping(value = "/{id}")
    public Reservation getById(@PathVariable int id) throws NotFoundException {
        return reservationService.findById(id);
    }

    @RequestMapping(value = "/{id}/car")
    public Car getCarById(@PathVariable int id) throws NotFoundException {
        Reservation reservation = reservationService.findById(id);
        return reservation.getCar();
    }

    @RequestMapping(value = "/{id}/customer")
    public Customer getCustomerById(@PathVariable int id) throws NotFoundException {
        Reservation reservation = reservationService.findById(id);
        return reservation.getCustomer();
    }

    @RequestMapping(value = "/{startDateString}/{endDateString}")
    public List<Reservation> getByDates(@PathVariable String startDateString, @PathVariable String endDateString) throws ParseException {
        Date startDate = dateService.parse(startDateString);
        Date endDate = dateService.parse(endDateString);

        return reservationService.findByDates(startDate, endDate);
    }

    @PostMapping
    public Reservation post(@RequestBody @Valid ExposedReservation exposedReservation) throws NotFoundException {
        Reservation reservation = exposedReservation.ToReservation();
        Car car = carService.findById(reservation.getCar().getId());

        long alreadyReservationsCount = car.getReservations().stream().filter(r -> dateService.isInBetween(r.getStartDate(), reservation.getStartDate(), reservation.getEndDate()) || dateService.isInBetween(r.getEndDate(), reservation.getStartDate(), reservation.getEndDate())).count();

        if(alreadyReservationsCount > 0)
            throw new NotFoundException("Car is not available for that period.");

        reservation.setCustomer(customerService.findById(reservation.getCustomer().getId()));
        reservation.setCar(car);

        Reservation savedReservation = reservationService.save(reservation);
        return savedReservation;
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) throws NotFoundException {
        reservationService.delete(id);
    }
}
