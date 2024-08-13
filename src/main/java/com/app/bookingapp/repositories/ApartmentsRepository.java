package com.app.bookingapp.repositories;


import com.app.bookingapp.models.Apartment;
import org.springframework.data.repository.CrudRepository;

//long - тип даних для ід
public interface ApartmentsRepository extends CrudRepository<Apartment, Long> {


}
