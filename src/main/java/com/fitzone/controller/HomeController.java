package com.fitzone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the home/landing page.
 */
@Controller
public class HomeController {

    /**
     * Displays the landing page with calls-to-action for Register and Dashboard.
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }
}
