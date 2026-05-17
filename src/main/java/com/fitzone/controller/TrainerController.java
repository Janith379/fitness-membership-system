package com.fitzone.controller;

import com.fitzone.model.Trainer;
import com.fitzone.service.TrainerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;

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
    public String processAdd(@ModelAttribute("trainer") Trainer trainer, @RequestParam("imageFile") MultipartFile imageFile, Model model) {
        if (!imageFile.isEmpty()) {
            String error = validateImage(imageFile);
            if (error != null) {
                model.addAttribute("error", error);
                return "trainer-form";
            }
            String fileName = trainerService.saveImage(imageFile);
            trainer.setImageUrl("/images/trainers/" + fileName);
        }
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
    public String processEdit(@PathVariable("id") String id, @ModelAttribute("trainer") Trainer trainer, @RequestParam("imageFile") MultipartFile imageFile, Model model) {
        if (!imageFile.isEmpty()) {
            String error = validateImage(imageFile);
            if (error != null) {
                model.addAttribute("error", error);
                return "trainer-form";
            }
            String fileName = trainerService.saveImage(imageFile);
            trainer.setImageUrl("/images/trainers/" + fileName);
        }
        trainerService.updateTrainer(id, trainer);
        return "redirect:/trainers/list";
    }

    private String validateImage(MultipartFile file) {
        if (file.getSize() > 10 * 1024 * 1024) {
            return "Image size is too large. Please upload an image below 10MB.";
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null) return "Invalid file.";
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!Arrays.asList("jpg", "jpeg", "png", "webp").contains(ext)) {
            return "Only image files (JPG, JPEG, PNG, WEBP) are allowed.";
        }
        return null;
    }

    @GetMapping("/delete/{id}")
    public String deleteTrainer(@PathVariable("id") String id) {
        trainerService.deleteTrainer(id);
        return "redirect:/trainers/list";
    }
}
