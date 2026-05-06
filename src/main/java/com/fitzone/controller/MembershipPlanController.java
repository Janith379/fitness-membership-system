package com.fitzone.controller;

import com.fitzone.model.MembershipPlan;
import com.fitzone.service.MembershipPlanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/plans")
public class MembershipPlanController {
    
    private final MembershipPlanService planService;

    public MembershipPlanController(MembershipPlanService planService) {
        this.planService = planService;
    }

    @GetMapping("/list")
    public String listPlans(Model model) {
        model.addAttribute("plans", planService.getAllPlans());
        return "plans-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("plan", new MembershipPlan());
        return "plan-form";
    }

    @PostMapping("/add")
    public String processAdd(@ModelAttribute("plan") MembershipPlan plan) {
        planService.createPlan(plan);
        return "redirect:/plans/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        Optional<MembershipPlan> plan = planService.findById(id);
        if (plan.isEmpty()) return "redirect:/plans/list";
        model.addAttribute("plan", plan.get());
        return "plan-form";
    }

    @PostMapping("/edit/{id}")
    public String processEdit(@PathVariable("id") String id, @ModelAttribute("plan") MembershipPlan plan) {
        planService.updatePlan(id, plan);
        return "redirect:/plans/list";
    }

    @GetMapping("/delete/{id}")
    public String deletePlan(@PathVariable("id") String id) {
        planService.deletePlan(id);
        return "redirect:/plans/list";
    }
}
