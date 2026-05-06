package com.fitzone.controller;

import com.fitzone.model.Trainer;
import com.fitzone.service.TrainerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/trainers")
public class TrainerController {
    
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping("/list")
    public String listTrainers(Model model) {
        model.addAttribute("trainers", trainerService.getAllTrainers());
        return "trainers-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("trainer", new Trainer());
        return "trainer-form";
    }

    @PostMapping("/add")
    public String processAdd(@ModelAttribute("trainer") Trainer trainer) {
        trainerService.createTrainer(trainer);
        return "redirect:/trainers/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        Optional<Trainer> trainer = trainerService.findById(id);
        if (trainer.isEmpty()) return "redirect:/trainers/list";
        model.addAttribute("trainer", trainer.get());
        return "trainer-form";
    }

    @PostMapping("/edit/{id}")
    public String processEdit(@PathVariable("id") String id, @ModelAttribute("trainer") Trainer trainer) {
        trainerService.updateTrainer(id, trainer);
        return "redirect:/trainers/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteTrainer(@PathVariable("id") String id) {
        trainerService.deleteTrainer(id);
        return "redirect:/trainers/list";
    }
}
