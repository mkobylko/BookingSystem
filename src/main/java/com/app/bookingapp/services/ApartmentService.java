package com.app.bookingapp.services;


import com.app.bookingapp.models.Apartment;
import com.app.bookingapp.models.User;
import com.app.bookingapp.repositories.ApartmentsRepository;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApartmentService {
    @Autowired
    private ApartmentsRepository apartmentsRepository;
    private final UserRepository userRepository;


    public void saveApartmentToDb(Principal principal, String name, String description, String country, String city, String street, double price, MultipartFile photo){

        //метод просто перетворює фото і ств екземпляр класу і зберігає в бд
        String photoFileName = StringUtils.cleanPath(photo.getOriginalFilename());

        if(photoFileName.contains("..")){
            System.out.println("not a valid file");
        }
        String EncodedPhotoFileName  = null;
        try {
            EncodedPhotoFileName = Base64.getEncoder().encodeToString(photo.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Apartment apartment = new Apartment(name, description, country, city, street, price, EncodedPhotoFileName);

        //principal.getName - name user/ по імейлу найти решту
        apartment.setUser(getUserByPrincipal(principal));

        //зберігання в бд
        apartmentsRepository.save(apartment);
    }

    //знаходження юезра по імейлу в бд
    public User getUserByPrincipal(Principal principal) {
        if(principal == null) return new User();
        return userRepository.findByEmail(principal.getName());

    }
    public Iterable<Apartment> getAllApartments()
    {
        return apartmentsRepository.findAll();
    }


   /* public Iterable<Apartment> getAvailableApartments(Principal principal, LocalDate arrivalDate, LocalDate departureDate, Iterable<Apartment> apartments) {
        // Якщо обидва параметри arrivalDate та departureDate є нульовими, повертаємо усі доступні апартаменти


        return availableApartments;
    }*/

}
