package com.fitzone.controller;

import com.fitzone.model.WorkoutSchedule;
import com.fitzone.service.MemberService;
import com.fitzone.service.TrainerService;
import com.fitzone.service.WorkoutScheduleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/workouts")
public class WorkoutScheduleController {
    
    private final WorkoutScheduleService scheduleService;
    private final MemberService memberService;
    private final TrainerService trainerService;

    public WorkoutScheduleController(WorkoutScheduleService scheduleService, MemberService memberService, TrainerService trainerService) {
        this.scheduleService = scheduleService;
        this.memberService = memberService;
        this.trainerService = trainerService;
    }

    @GetMapping("/list")
    public String listSchedules(Model model) {
        model.addAttribute("schedules", scheduleService.getAllSchedules());
        return "workouts-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("schedule", new WorkoutSchedule());
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("trainers", trainerService.getAllTrainers());
        return "workout-form";
    }

    @PostMapping("/add")
    public String processAdd(@ModelAttribute("schedule") WorkoutSchedule schedule) {
        scheduleService.createSchedule(schedule);
        return "redirect:/workouts/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        Optional<WorkoutSchedule> schedule = scheduleService.findById(id);
        if (schedule.isEmpty()) return "redirect:/workouts/list";
        model.addAttribute("schedule", schedule.get());
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("trainers", trainerService.getAllTrainers());
        return "workout-form";
    }

    @PostMapping("/edit/{id}")
    public String processEdit(@PathVariable("id") String id, @ModelAttribute("schedule") WorkoutSchedule schedule) {
        scheduleService.updateSchedule(id, schedule);
        return "redirect:/workouts/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable("id") String id) {
        scheduleService.deleteSchedule(id);
        return "redirect:/workouts/list";
    }
}
