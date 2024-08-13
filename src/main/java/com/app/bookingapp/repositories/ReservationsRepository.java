package com.app.bookingapp.repositories;


import com.app.bookingapp.models.Reservations;
import com.app.bookingapp.models.User;
import org.springframework.data.repository.CrudRepository;

//long - тип даних для ід
public interface ReservationsRepository extends CrudRepository<Reservations, Long> {
    Iterable<Reservations> findByApartmentId(long apartmentId);
}