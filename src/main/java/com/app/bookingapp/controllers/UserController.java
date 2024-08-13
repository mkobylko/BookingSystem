package com.app.bookingapp.controllers;

import com.app.bookingapp.models.Apartment;
import com.app.bookingapp.models.User;
import com.app.bookingapp.repositories.ApartmentsRepository;
import com.app.bookingapp.repositories.UserRepository;
import com.app.bookingapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private ApartmentsRepository apartmentsRepository;
    @Autowired
    private UserRepository userRepository;


    //перехід на сторінку login
    @GetMapping("/login")
    public String login(){
        return "login";
    }


    @GetMapping("/registration")
    public String registration(){
        return "registration-form";
    }


    @PostMapping("/registration")
    public String createUser(User user) {
        userService.createUser(user);
        return "redirect:/login";
    }

    @GetMapping("/user/{user}")
    public String userInfoAdmin(@PathVariable("user") User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("apartments", user.getApartments());
        return "user-details";
    }



    //визначення ід залогіненого користувача на сторінці контролера
    @GetMapping("/user/about")
    public String userAbout(Model model) {
        // Отримання поточного користувача через Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("currentUser", userDetails);

            // Отримання ідентифікатора поточного користувача
            String userEmail = userDetails.getUsername();

            // Отримання користувача за його email
            User currentUser = userRepository.findByEmail(userEmail);

            // Перевірка чи користувач знайдений
            if (currentUser != null) {
                // Отримання апартаментів користувача
                List<Apartment> apartments = currentUser.getApartments();
                model.addAttribute("apartments", apartments);
            } else {
                // Якщо користувача не знайдено, повернути помилку
                return "redirect:/error";
            }
        } else {
            return "redirect:/login";
        }

        return "user-details";
    }



}
