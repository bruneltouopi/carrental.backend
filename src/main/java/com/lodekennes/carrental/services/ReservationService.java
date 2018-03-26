package com.lodekennes.carrental.services;

import com.lodekennes.carrental.exceptions.NotFoundException;
import com.lodekennes.carrental.models.Reservation;
import com.lodekennes.carrental.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    public Iterable<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation findById(int id) throws NotFoundException {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);

        if(!optionalReservation.isPresent())
            throw new NotFoundException("Reservation not found.");
        return optionalReservation.get();
    }

    public List<Reservation> findByDates(Date startDate, Date endDate) {
        return reservationRepository.findByDates(startDate, endDate);
    }

    public final Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public final void delete(int id) throws NotFoundException {
        try {
            reservationRepository.deleteById(id);
        } catch(EmptyResultDataAccessException e) {
            throw new NotFoundException("Customer not found.");
        }
    }
}
