package com.app.bookingapp.controllers;


import com.app.bookingapp.models.Reservations;
import com.app.bookingapp.repositories.ReservationsRepository;
import com.app.bookingapp.services.ApartmentService;
import com.app.bookingapp.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
public class ReservationsController {

    @Autowired
    private ReservationsRepository reservationsRepository;
    @Autowired
    private ReservationService reservationService;

    @PostMapping("/reservation/add")
    public String apartmentAnnouncementAdd(Principal principal,
                                           @RequestParam("arrivalDate") @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate arrivalDate,
                                           @RequestParam("departureDate") @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate departureDate,
                                           @RequestParam double price,
                                           @RequestParam long apartment_id,
                                           Model model){

        reservationService.saveReservationToDb(principal, arrivalDate, departureDate, price, apartment_id);
        return "redirect:/stays";
    }

}
