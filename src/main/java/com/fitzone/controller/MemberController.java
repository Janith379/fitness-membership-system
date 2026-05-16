package com.fitzone.controller;

import com.fitzone.model.*;
import com.fitzone.service.MemberService;
import com.fitzone.service.MembershipPlanService;
import com.fitzone.service.PaymentService;
import com.fitzone.service.WorkoutScheduleService;
import com.fitzone.service.DietPlanService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Controller handling member CRUD operations:
 * registration, listing, viewing, updating, deleting, and searching.
 */
@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final PaymentService paymentService;
    private final MembershipPlanService planService;
    private final WorkoutScheduleService workoutScheduleService;
    private final DietPlanService dietPlanService;

    public MemberController(MemberService memberService, PaymentService paymentService,
                            MembershipPlanService planService, WorkoutScheduleService workoutScheduleService,
                            DietPlanService dietPlanService) {
        this.memberService = memberService;
        this.paymentService = paymentService;
        this.planService = planService;
        this.workoutScheduleService = workoutScheduleService;
        this.dietPlanService = dietPlanService;
    }

    /**
     * Displays the registration form.
     * If the user is returning from the payment page, pre-fill with session data.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model, HttpSession session) {
        RegistrationForm existingForm = (RegistrationForm) session.getAttribute("registrationForm");
        if (existingForm != null) {
            model.addAttribute("registrationForm", existingForm);
        } else {
            model.addAttribute("registrationForm", new RegistrationForm());
        }
        model.addAttribute("plans", planService.getAllPlans());
        return "register";
    }

    /**
     * Processes the registration form.
     * On success, stores form data in session and redirects to payment page.
     */
    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("registrationForm") RegistrationForm form,
                                      BindingResult result,
                                      Model model,
                                      HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("plans", planService.getAllPlans());
            return "register";
        }

        // Store registration data in session for the payment step
        session.setAttribute("registrationForm", form);

        // Calculate preview amounts using selected plan and duration-based discounts
        double monthlyFee = 0.0;
        Optional<MembershipPlan> selectedPlan = planService.getAllPlans().stream()
                .filter(p -> p.getPlanName().equalsIgnoreCase(form.getMembershipType()))
                .findFirst();
        if (selectedPlan.isPresent()) {
            monthlyFee = selectedPlan.get().getMonthlyFee();
        }

        double originalAmount = monthlyFee * form.getDurationMonths();
        double discountPercentage = 0.0;
        if (form.getDurationMonths() >= 6) {
            discountPercentage = 0.15;
        } else if (form.getDurationMonths() >= 3) {
            discountPercentage = 0.10;
        }

        double discountAmount = originalAmount * discountPercentage;
        double totalAmount = originalAmount - discountAmount;

        session.setAttribute("monthlyFee", monthlyFee);
        session.setAttribute("originalAmount", originalAmount);
        session.setAttribute("discountPercentage", (int)(discountPercentage * 100));
        session.setAttribute("discountAmount", discountAmount);
        session.setAttribute("totalAmount", totalAmount);

        return "redirect:/payment";
    }

    /**
     * Lists all members with optional search.
     */
    @GetMapping("/list")
    public String listMembers(@RequestParam(value = "query", required = false) String query,
                              Model model) {
        List<Member> members;
        if (query != null && !query.trim().isEmpty()) {
            members = memberService.searchMembers(query);
            model.addAttribute("searchQuery", query);
        } else {
            members = memberService.getAllMembers();
        }
        model.addAttribute("members", members);
        return "members-list";
    }

    /**
     * Shows full details of a specific member, including their payments.
     */
    @GetMapping("/view/{id}")
    public String viewMember(@PathVariable("id") String id, Model model,
                             RedirectAttributes redirectAttributes) {
        Optional<Member> memberOpt = memberService.findById(id);
        if (memberOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Member not found: " + id);
            return "redirect:/members/list";
        }
        model.addAttribute("member", memberOpt.get());
        // Fetch payments for this member
        List<Payment> payments = paymentService.getPaymentsByMemberId(id);
        model.addAttribute("payments", payments);

        // Fetch workouts and diets for this member
        model.addAttribute("workouts", workoutScheduleService.findByMemberId(id));
        model.addAttribute("diets", dietPlanService.findByMemberId(id));

        return "member-detail";
    }

    /**
     * Shows the edit form pre-filled with existing member data.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model,
                               RedirectAttributes redirectAttributes) {
        Optional<Member> memberOpt = memberService.findById(id);
        if (memberOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Member not found: " + id);
            return "redirect:/members/list";
        }
        Member member = memberOpt.get();

        // Pre-fill the form with existing data
        RegistrationForm form = new RegistrationForm();
        form.setFullName(member.getFullName());
        form.setEmail(member.getEmail());
        form.setPhone(member.getPhone());
        form.setAge(member.getAge());
        form.setGender(member.getGender());
        form.setMembershipType(member.getMembershipType());
        form.setDurationMonths(member.getDurationMonths());
        form.setNotes(member.getNotes());
        form.setPassword(member.getPassword());
        form.setConfirmPassword(member.getPassword());

        model.addAttribute("registrationForm", form);
        model.addAttribute("memberId", id);
        model.addAttribute("plans", planService.getAllPlans());
        return "member-update";
    }

    /**
     * Processes the member update form.
     */
    @PostMapping("/edit/{id}")
    public String processUpdate(@PathVariable("id") String id,
                                @Valid @ModelAttribute("registrationForm") RegistrationForm form,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        // Special handling for updates: ignore password validation errors.
        // Old members might have simple passwords (like phone numbers) that don't match the new regex.
        // Since the admin is not changing the password here, we shouldn't block the update.
        if (result.hasFieldErrors("password") || result.hasFieldErrors("confirmPassword")) {
            // Check if these are the ONLY errors or if there are other validation issues
            boolean otherErrors = result.getFieldErrors().stream()
                    .anyMatch(e -> !e.getField().toLowerCase().contains("password"));

            if (!otherErrors) {
                // If only password errors exist, we proceed with the update
                memberService.updateMember(id, form);
                redirectAttributes.addFlashAttribute("successMessage", "Member " + id + " updated successfully!");
                return "redirect:/members/list";
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("memberId", id);
            model.addAttribute("plans", planService.getAllPlans());
            return "member-update";
        }

        memberService.updateMember(id, form);
        redirectAttributes.addFlashAttribute("successMessage", "Member " + id + " updated successfully!");
        return "redirect:/members/list";
    }

    /**
     * Deletes a member and their associated payments.
     */
    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable("id") String id,
                               RedirectAttributes redirectAttributes) {
        Optional<Member> memberOpt = memberService.findById(id);
        if (memberOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Member not found: " + id);
            return "redirect:/members/list";
        }

        // Delete associated payments first, then the member
        paymentService.deletePaymentsByMemberId(id);
        memberService.deleteMember(id);

        redirectAttributes.addFlashAttribute("successMessage",
                "Member " + id + " and associated payments deleted successfully!");
        return "redirect:/members/list";
    }
}
