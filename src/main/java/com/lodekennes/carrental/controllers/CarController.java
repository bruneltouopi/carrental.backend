package com.lodekennes.carrental.controllers;

import com.lodekennes.carrental.exceptions.NotFoundException;
import com.lodekennes.carrental.models.Car;
import com.lodekennes.carrental.models.Reservation;
import com.lodekennes.carrental.repositories.CarRepository;
import com.lodekennes.carrental.services.CarService;
import com.lodekennes.carrental.services.DateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private DateService dateService;

    @RequestMapping
    public Iterable<Car> get() {
        return carService.findAll();
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public Car get(@PathVariable int id) throws NotFoundException {
        return carService.findById(id);
    }

    @RequestMapping(value="/{id}/reservations", method = RequestMethod.GET)
    public Set<Reservation> getReservations(@PathVariable int id) throws NotFoundException {
        return carService.findById(id).getReservations();
    }

    @RequestMapping(value = "/{startDateString}/{endDateString}")
    public List<Car> getAvailable(@PathVariable String startDateString, @PathVariable String endDateString) throws ParseException {
        Date startDate = dateService.parse(startDateString);
        Date endDate = dateService.parse(endDateString);

        return carService.findAvailable(startDate, endDate);
    }

    @PostMapping
    public Car post(@RequestBody @Valid Car car) throws Exception {
        return carService.save(car);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Car put(@PathVariable int id, @RequestBody Car newCar) throws NotFoundException {
        Car car = carService.findById(id);

        if(1 <= newCar.getName().length()) {
            car.setName(newCar.getName());
        }

        if(1 <= car.getPricePerDay()) {
            car.setPricePerDay(newCar.getPricePerDay());
        }

        return carService.save(car);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) throws NotFoundException {
        carService.delete(id);
    }
}
