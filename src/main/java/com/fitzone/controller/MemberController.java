package com.fitzone.controller;

import com.fitzone.model.*;
import com.fitzone.service.MemberService;
import com.fitzone.service.PaymentService;
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

    public MemberController(MemberService memberService, PaymentService paymentService) {
        this.memberService = memberService;
        this.paymentService = paymentService;
    }

    /**
     * Displays the registration form.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
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
            return "register";
        }

        // Store registration data in session for the payment step
        session.setAttribute("registrationForm", form);

        // Calculate preview amounts using polymorphism
        double monthlyFee;
        if ("Premium".equalsIgnoreCase(form.getMembershipType())) {
            PremiumMember temp = new PremiumMember();
            monthlyFee = temp.calculateMonthlyFee();
        } else {
            RegularMember temp = new RegularMember();
            monthlyFee = temp.calculateMonthlyFee();
        }
        double totalAmount = monthlyFee * form.getDurationMonths();

        session.setAttribute("monthlyFee", monthlyFee);
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

        model.addAttribute("registrationForm", form);
        model.addAttribute("memberId", id);
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
        if (result.hasErrors()) {
            model.addAttribute("memberId", id);
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
