package com.example.fitnesscentersystest1.controller;

import com.example.fitnesscentersystest1.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MemberController {

    MemberService service=new MemberService();

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }

    @PostMapping("/register")
    public String registerMember(@RequestParam int id,
                                 @RequestParam String name,
                                 @RequestParam String plan,
                                 @RequestParam String duration){

        service.registerMember(id,name,plan,duration);

        return "redirect:/members";
    }

    @GetMapping("/members")
    public String viewMembers(Model model){

        model.addAttribute("members",service.getMembers());

        return "members";
    }

}