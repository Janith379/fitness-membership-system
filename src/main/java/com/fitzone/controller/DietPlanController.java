package com.fitzone.controller;

import com.fitzone.model.DietPlan;
import com.fitzone.service.DietPlanService;
import com.fitzone.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/diets")
public class DietPlanController {
    
    private final DietPlanService dietPlanService;
    private final MemberService memberService;

    public DietPlanController(DietPlanService dietPlanService, MemberService memberService) {
        this.dietPlanService = dietPlanService;
        this.memberService = memberService;
    }

    @GetMapping("/list")
    public String listDietPlans(Model model) {
        model.addAttribute("diets", dietPlanService.getAllDietPlans());
        return "diets-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("diet", new DietPlan());
        model.addAttribute("members", memberService.getAllMembers());
        return "diet-form";
    }

    @PostMapping("/add")
    public String processAdd(@ModelAttribute("diet") DietPlan dietPlan) {
        dietPlanService.createDietPlan(dietPlan);
        return "redirect:/diets/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        Optional<DietPlan> dietPlan = dietPlanService.findById(id);
        if (dietPlan.isEmpty()) return "redirect:/diets/list";
        model.addAttribute("diet", dietPlan.get());
        model.addAttribute("members", memberService.getAllMembers());
        return "diet-form";
    }

    @PostMapping("/edit/{id}")
    public String processEdit(@PathVariable("id") String id, @ModelAttribute("diet") DietPlan dietPlan) {
        dietPlanService.updateDietPlan(id, dietPlan);
        return "redirect:/diets/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteDietPlan(@PathVariable("id") String id) {
        dietPlanService.deleteDietPlan(id);
        return "redirect:/diets/list";
    }
}
