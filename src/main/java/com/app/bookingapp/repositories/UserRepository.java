package com.app.bookingapp.repositories;


import com.app.bookingapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

//long - тип даних для ід
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
