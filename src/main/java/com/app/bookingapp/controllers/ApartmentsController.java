package com.app.bookingapp.controllers;

import com.app.bookingapp.models.Apartment;
import com.app.bookingapp.models.Reservations;
import com.app.bookingapp.repositories.ApartmentsRepository;
import com.app.bookingapp.services.ApartmentService;
import com.app.bookingapp.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ApartmentsController {

    //силання на репозиторій
    @Autowired
    private ApartmentsRepository apartmentsRepository;

    //для сервер
    @Autowired
    private ApartmentService apartmentServices;
    @Autowired
    private ReservationService reservationService;


    @GetMapping("/stays")
    public String stays(Model model, Principal principal,
                        @RequestParam(value = "arrivalDate", required = false) String arrivalDateString,
                        @RequestParam(value = "departureDate", required = false) String departureDateString) {

        LocalDate arrivalDate = null;
        LocalDate departureDate = null;

        if (arrivalDateString != null && !arrivalDateString.isEmpty()) {
            arrivalDate = LocalDate.parse(arrivalDateString, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
        if (departureDateString != null && !departureDateString.isEmpty()) {
            departureDate = LocalDate.parse(departureDateString, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }

        Iterable<Apartment> apartments = apartmentServices.getAllApartments();
        Iterable<Reservations> reservations = reservationService.getAllReservations();

        if (arrivalDate != null && departureDate != null) {

            List<Apartment> availableApartments = new ArrayList<>();
            for (Apartment apartment : apartments) {

                boolean isAvailable = true;
                for (Reservations reservation : reservations) {
                    if (reservation.getApartment().getId() == apartment.getId() &&
                            ((reservation.getArrivalDate().isBefore(departureDate) && reservation.getDepartureDate().isAfter(arrivalDate)) ||
                                    (reservation.getArrivalDate().isEqual(arrivalDate) && reservation.getDepartureDate().isEqual(departureDate)) ||
                                    (reservation.getArrivalDate().isBefore(arrivalDate) && reservation.getDepartureDate().isAfter(arrivalDate)))) {
                        // Якщо апартамент заброньовано хоча б на одну дату в діапазоні arrivalDate і departureDate, він не доступний
                        isAvailable = false;
                        break;
                    }
                }
                if (isAvailable) {
                    availableApartments.add(apartment);
                }
            }
            apartments = availableApartments;
        }

        model.addAttribute("apartments", apartments);
        model.addAttribute("reservations", reservations);
        model.addAttribute("user", apartmentServices.getUserByPrincipal(principal));

        return "stays";
    }






    //перехід на сторінку додавання оголошення
    @GetMapping("/stays/add")
    //метод - повертає строку - назву шаблонна який маємо отримати
    public String staysAdd(Model model){
        return "staysAdd";
    }

    //отрмання данизх з форми
    //додавання даних в бд
    @PostMapping("/stays/add")         //назва як в атрибуті name - staysAdd.html
    public String apartmentAnnouncementAdd(Principal principal, @RequestParam String name, @RequestParam String description,
                                           @RequestParam String country, @RequestParam String city, @RequestParam String street,
                                           @RequestParam double price, @RequestParam("photo") MultipartFile photo, Model model){

        apartmentServices.saveApartmentToDb(principal, name, description, country, city, street, price, photo);
        return "redirect:/stays";
    }

    //перехід на сторінку з оголошенням
    @GetMapping("/stays/{id}")
    public String staysDetails(@PathVariable(value = "id") long id, Model model){
        // Перевірка на існування ідентифікатора. Якщо його немає, перенаправлення.
        if(!apartmentsRepository.existsById(id)){
            return "redirect:/stays";
        }

        // Отримання поточного користувача через Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("currentUser", userDetails);
        } else {
            return "redirect:/login";
        }

        ArrayList<Apartment> res = new ArrayList<>();
        // Отримання апартаментів за ідентифікатором
        Optional<Apartment> stays = apartmentsRepository.findById(id);
        // Додавання апартаментів до моделі
        stays.ifPresent(res::add);
        model.addAttribute("apartments", res);

        return "apartment-details"; // Повернення назви шаблону
    }

    //перехід на сторінку редагувння оголошення
    @GetMapping("/stays/{id}/update")
    //метод - повертає строку - назву шаблонна який маємо отримати
    public String staysEdit(@PathVariable(value = "id") long id, Model model){
        //перевірка на існуючість ід. якщо фолс то редірект
        if(!apartmentsRepository.existsById(id)){
            return "redirect:/stays";
        }

        //отримує одну запісь і її передаємо у шаблон
        ArrayList<Apartment> res = new ArrayList<>();
        //запис треба помістити в об'єкт
        Optional<Apartment> stays = apartmentsRepository.findById(id);
        //превод з класу Optional в ArrayList
        stays.ifPresent(res::add);
        model.addAttribute("apartment", res);
        return "apartment-edit";

    }

    //оновлення даних в бд
    @PostMapping("/stays/{id}/update")
    public String apartmentEdit(@PathVariable(value = "id") long id, @RequestParam String nameApartments, @RequestParam String description,
                                           @RequestParam String country, @RequestParam String city,@RequestParam String street,
                                           @RequestParam double price, Model model){

        Apartment stays = apartmentsRepository.findById(id).orElseThrow();
        stays.setName(nameApartments);
        stays.setDescription(description);
        stays.setCountry(country);
        stays.setCity(city);
        stays.setStreet(street);
        stays.setPrice(price);

        //оновлюється існуючий обєкт
        apartmentsRepository.save(stays);

        return "redirect:/stays";
    }

    //видалення даних з бд
    @PostMapping("/stays/{id}/delete")
    public String apartmentDelete(@PathVariable(value = "id") long id, Model model){

        Apartment stays = apartmentsRepository.findById(id).orElseThrow();
        apartmentsRepository.delete(stays);

        return "redirect:/stays";
    }
}
