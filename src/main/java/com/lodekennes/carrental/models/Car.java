package com.lodekennes.carrental.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lodekennes.carrental.serializers.SetReservationSerializer;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Car {
    private int id;
    @NotBlank
    private String name;
    @Range(min = 1)
    private double pricePerDay;
    private Set<Reservation> reservations;

    public Car() {
        this("", 1);
    }

    public Car(String name, double pricePerDay) {
        this.name = name;
        this.pricePerDay = pricePerDay;
        this.reservations = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonSerialize(using = SetReservationSerializer.class)
    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
}
