package com.lodekennes.carrental.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Reservation {

    public Reservation() {}

    public Reservation(Date startDate, Date endDate, Customer customer, Car car, boolean isPaid) {
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setCustomer(customer);
        this.setCar(car);
        this.setPaid(isPaid);
    }

    private int id;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @NotNull
    private Customer customer;
    @NotNull
    private Car car;
    @NotNull
    private boolean paid;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "car_id")
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
