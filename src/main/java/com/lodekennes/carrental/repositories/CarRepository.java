package com.lodekennes.carrental.repositories;

import com.lodekennes.carrental.models.Car;
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
public interface CarRepository extends CrudRepository<Car, Integer> {
    @Query("SELECT c FROM Car c WHERE c.id NOT IN " +
            "(SELECT r.car FROM Reservation r WHERE " +
            "   (" +
            "       (YEAR(:startDate) >= YEAR(r.startDate) AND YEAR(:startDate) <= YEAR(r.endDate))" +
            "       AND" +
            "       (MONTH(:startDate) >= MONTH(r.startDate) AND MONTH(:startDate) <= MONTH(r.endDate))" +
            "       AND" +
            "       (DAY(:startDate) >= DAY(r.startDate) AND DAY(:startDate) <= DAY(r.endDate))" +
            "   OR" +
            "       (YEAR(:endDate) >= YEAR(r.startDate) AND YEAR(:endDate) <= YEAR(r.endDate))" +
            "       AND" +
            "       (MONTH(:endDate) >= MONTH(r.startDate) AND MONTH(:endDate) <= MONTH(r.endDate))" +
            "       AND" +
            "       (DAY(:endDate) >= DAY(r.startDate) AND DAY(:endDate) <= DAY(r.endDate))" +
            "   )" +
            ")")
    List<Car> findAvailable(@Param("startDate") @Temporal(TemporalType.DATE) Date startDate, @Param("endDate") @Temporal(TemporalType.DATE) Date endDate);
}
