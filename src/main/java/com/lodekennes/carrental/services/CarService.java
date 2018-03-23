package com.lodekennes.carrental.services;

import com.lodekennes.carrental.exceptions.NotFoundException;
import com.lodekennes.carrental.models.Car;
import com.lodekennes.carrental.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    public final Car save(final Car car) {
        return carRepository.save(car);
    }

    public final Iterable<Car> findAll() {
        return carRepository.findAll();
    }

    public final Car findById(int id) throws NotFoundException {
        Optional<Car> car = carRepository.findById(id);

        if(!car.isPresent())
            throw new NotFoundException("Car not found.");
        return car.get();
    }

    public final List<Car> findAvailable(Date startDare, Date endDate) {
        return carRepository.findAvailable(startDare, endDate);
    }

    public final void delete(int id) throws NotFoundException {
        Car c = findById(id);
        carRepository.delete(c);
    }
}
