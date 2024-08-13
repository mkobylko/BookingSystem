package com.app.bookingapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    //яка саме урл адреса оброблюється
    // /- головна сторінка
    @GetMapping("/")
    //викликаєється хтмл шаблон home(створений мною)
    public String home(Model model) {
        model.addAttribute("title", "main page");
        return "home";
    }

}