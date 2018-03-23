package com.lodekennes.carrental.repositories;

import com.lodekennes.carrental.models.Car;
import com.lodekennes.carrental.models.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
    @Query("SELECT r FROM Reservation r WHERE YEAR(r.startDate) = YEAR(:startDate) AND MONTH(r.startDate) = MONTH(:startDate) AND DAY(r.startDate) = DAY(:startDate) AND YEAR(r.endDate) = YEAR(:endDate) AND MONTH(r.endDate) = MONTH(:endDate) AND DAY(r.endDate) = DAY(:endDate)")
    List<Reservation> findByDates(@Param("startDate") @Temporal(TemporalType.DATE) Date startDate, @Param("endDate") @Temporal(TemporalType.DATE) Date endDate);
}
