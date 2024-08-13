package com.app.bookingapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservations {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // private long apartmentId;

    private LocalDate arrivalDate;
    private LocalDate departureDate;

    private String status;

    private double sumPrice;

    //private int quantityOfGuests;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private Apartment apartment;



    public Reservations(LocalDate arrivalDate, LocalDate departureDate, String status, double sumPrice, Apartment apartment) {
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.status = status;
        this.sumPrice = sumPrice;
        this.apartment = apartment;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public String getStatus() {
        return status;
    }

    public double getSumPrice() {
        return sumPrice;
    }


    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSumPrice(double sumPrice) {
        this.sumPrice = sumPrice;
    }


}
