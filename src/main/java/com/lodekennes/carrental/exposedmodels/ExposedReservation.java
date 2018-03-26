package com.lodekennes.carrental.exposedmodels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lodekennes.carrental.models.Car;
import com.lodekennes.carrental.models.Customer;
import com.lodekennes.carrental.models.Reservation;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class ExposedReservation {
    private int id;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    @Range(min = 1L)
    private int customer;
    @Range(min = 1L)
    private int car;
    @NotNull
    private boolean paid;

    public ExposedReservation() {}

    public ExposedReservation(Date startDate, Date endDate, int customer, int car, boolean paid) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.customer = customer;
        this.customer = car;
        this.paid = paid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public int getCar() {
        return car;
    }

    public void setCar(int car) {
        this.car = car;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @JsonIgnore
    public Reservation ToReservation() {
        Customer customer = new Customer();
        customer.setId(this.customer);

        Car car = new Car();
        car.setId(this.car);

        Reservation reservation = new Reservation(startDate, endDate, customer, car, paid);

        reservation.setId(id);

        return reservation;
    }
}
