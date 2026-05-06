package com.fitzone.controller;

import com.fitzone.model.Member;
import com.fitzone.model.Payment;
import com.fitzone.service.MemberService;
import com.fitzone.service.PaymentService;
import com.fitzone.service.WorkoutScheduleService;
import com.fitzone.service.DietPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WorkoutScheduleService workoutScheduleService;

    @Autowired
    private DietPlanService dietPlanService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // The username is the email

        Optional<Member> memberOpt = memberService.getAllMembers().stream()
                .filter(m -> m.getEmail().equalsIgnoreCase(email))
                .findFirst();

        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            model.addAttribute("member", member);

            // Calculate remaining days and status
            long remainingDays = 0;
            String status = "Expired";
            if (member.getExpiryDate() != null) {
                remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), member.getExpiryDate());
                if (remainingDays >= 0) {
                    status = "Active";
                } else {
                    remainingDays = 0;
                }
            }
            model.addAttribute("remainingDays", remainingDays);
            model.addAttribute("status", status);

            // Get payments
            List<Payment> payments = paymentService.getAllPayments().stream()
                    .filter(p -> p.getMemberId().equals(member.getMemberId()))
                    .collect(Collectors.toList());
            model.addAttribute("payments", payments);

            model.addAttribute("workouts", workoutScheduleService.findByMemberId(member.getMemberId()));
            model.addAttribute("diets", dietPlanService.findByMemberId(member.getMemberId()));

            return "dashboard";
        }

        return "redirect:/login?error";
    }
}
