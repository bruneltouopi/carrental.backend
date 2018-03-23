package com.lodekennes.carrental.controllers;

import com.lodekennes.carrental.exceptions.NotFoundException;
import com.lodekennes.carrental.models.Car;
import com.lodekennes.carrental.models.Reservation;
import com.lodekennes.carrental.repositories.CarRepository;
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
@RequestMapping("/api/v1/cars")
public class CarController {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private DateService dateService;

    @RequestMapping
    public Iterable<Car> get() {
        Iterable<Car> cars = carRepository.findAll();
        return cars;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public Car get(@PathVariable int id) throws NotFoundException {
        Optional<Car> optionalCar = carRepository.findById(id);

        if(!optionalCar.isPresent())
            throw new NotFoundException("Car not found.");

        return optionalCar.get();
    }

    @RequestMapping(value="/{id}/reservations", method = RequestMethod.GET)
    public Set<Reservation> getReservations(@PathVariable int id) throws NotFoundException {
        Optional<Car> optionalCar = carRepository.findById(id);

        if(!optionalCar.isPresent())
            throw new NotFoundException("Car not found.");

        return optionalCar.get().getReservations();
    }

    @RequestMapping(value = "/{startDateString}/{endDateString}")
    public List<Car> getAvailable(@PathVariable String startDateString, @PathVariable String endDateString) throws ParseException {
        Date startDate = dateService.parse(startDateString);
        Date endDate = dateService.parse(endDateString);

        List<Car> availableCars = carRepository.findAvailable(startDate, endDate);
        return availableCars;
    }

    @PostMapping
    public Car post(@RequestBody @Valid Car car) throws Exception {
        return carRepository.save(car);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Car put(@PathVariable int id, @RequestBody Car newCar) throws NotFoundException {
        Optional<Car> optionalCar = carRepository.findById(id);

        if(!optionalCar.isPresent())
            throw new NotFoundException("Car not found.");

        Car car = optionalCar.get();

        if(1 <= newCar.getName().length()) {
            car.setName(newCar.getName());
        }

        if(1 <= car.getPricePerDay()) {
            car.setPricePerDay(newCar.getPricePerDay());
        }

        carRepository.save(car);

        return car;
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) throws NotFoundException {
        Optional<Car> optionalCar = carRepository.findById(id);

        if(!optionalCar.isPresent())
            throw new NotFoundException("Car not found.");

        carRepository.delete(optionalCar.get());
    }
}
