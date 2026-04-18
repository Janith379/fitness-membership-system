//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fitzone.controller;

import com.fitzone.model.Member;
import com.fitzone.model.Payment;
import com.fitzone.model.PaymentForm;
import com.fitzone.model.RegistrationForm;
import com.fitzone.service.MemberService;
import com.fitzone.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"/payment"})
public class PaymentController {
    private final PaymentService paymentService;
    private final MemberService memberService;

    public PaymentController(PaymentService paymentService, MemberService memberService) {
        this.paymentService = paymentService;
        this.memberService = memberService;
    }

    @GetMapping
    public String showPaymentForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        RegistrationForm regForm = (RegistrationForm)session.getAttribute("registrationForm");
        if (regForm == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please complete registration first.");
            return "redirect:/members/register";
        } else {
            Double monthlyFee = (Double)session.getAttribute("monthlyFee");
            Double totalAmount = (Double)session.getAttribute("totalAmount");
            model.addAttribute("registrationForm", regForm);
            model.addAttribute("monthlyFee", monthlyFee);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("paymentForm", new PaymentForm());
            return "payment";
        }
    }

    @PostMapping({"/process"})
    public String processPayment(@ModelAttribute("paymentForm") @Valid PaymentForm paymentForm, BindingResult result, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        RegistrationForm regForm = (RegistrationForm)session.getAttribute("registrationForm");
        if (regForm == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Session expired. Please register again.");
            return "redirect:/members/register";
        } else {
            Double totalAmount = (Double)session.getAttribute("totalAmount");
            Double monthlyFee = (Double)session.getAttribute("monthlyFee");
            if (result.hasErrors()) {
                model.addAttribute("registrationForm", regForm);
                model.addAttribute("monthlyFee", monthlyFee);
                model.addAttribute("totalAmount", totalAmount);
                return "payment";
            } else {
                LocalDate paymentDate = LocalDate.now();
                Member member = this.memberService.createMember(regForm, paymentDate);
                Payment payment = this.paymentService.processPayment(paymentForm, member.getMemberId(), totalAmount);
                if ("SUCCESS".equals(payment.getStatus())) {
                    session.removeAttribute("registrationForm");
                    session.removeAttribute("monthlyFee");
                    session.removeAttribute("totalAmount");
                    redirectAttributes.addFlashAttribute("member", member);
                    redirectAttributes.addFlashAttribute("payment", payment);
                    redirectAttributes.addFlashAttribute("totalAmount", totalAmount);
                    return "redirect:/payment/success";
                } else {
                    this.memberService.deleteMember(member.getMemberId());
                    redirectAttributes.addFlashAttribute("failureReason", "Payment declined. Only cards starting with 4 (Visa) or 5 (MasterCard) are accepted.");
                    redirectAttributes.addFlashAttribute("registrationForm", regForm);
                    return "redirect:/payment/failed";
                }
            }
        }
    }

    @GetMapping({"/success"})
    public String paymentSuccess(Model model) {
        return !model.containsAttribute("payment") ? "redirect:/" : "payment-success";
    }

    @GetMapping({"/failed"})
    public String paymentFailed(Model model) {
        return !model.containsAttribute("failureReason") ? "redirect:/" : "payment-failed";
    }
}
