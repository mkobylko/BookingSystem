package com.app.bookingapp.services;


import com.app.bookingapp.models.Apartment;
import com.app.bookingapp.models.Reservations;
import com.app.bookingapp.models.User;
import com.app.bookingapp.repositories.ApartmentsRepository;
import com.app.bookingapp.repositories.ReservationsRepository;
import com.app.bookingapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationService {
    @Autowired
    private final ReservationsRepository reservationsRepository;
    @Autowired
    private final ApartmentsRepository apartmentsRepository;
    @Autowired
    private final UserRepository userRepository;

    public Iterable<Reservations> getAllReservations()
    {
        return reservationsRepository.findAll();
    }
    public void saveReservationToDb(Principal principal, LocalDate arrivalDate, LocalDate departureDate, double price, long apartmentId) {
        long daysBetween = ChronoUnit.DAYS.between(arrivalDate, departureDate);

        //Available apartment
        Iterable<Reservations> reservations = reservationsRepository.findByApartmentId(apartmentId);
        for (Reservations reservation : reservations) {
            if (reservation.getArrivalDate().isBefore(departureDate) && reservation.getDepartureDate().isAfter(arrivalDate)) {
                throw new RuntimeException("This apartment is already booked for the specified dates");
            }
        }

        //apartment id
        Apartment apartment = apartmentsRepository.findById(apartmentId)
                .orElseThrow(() -> new IllegalArgumentException("Apartment not found"));
        Reservations reservation = new Reservations(arrivalDate, departureDate, "booked", daysBetween * price, apartment);

        //userid
        reservation.setUser(getUserByPrincipal(principal));

        reservationsRepository.save(reservation);
    }


    //знаходження юезра по імейлу в бд
    public User getUserByPrincipal(Principal principal) {
        if(principal == null) return new User();
        return userRepository.findByEmail(principal.getName());

    }



}

