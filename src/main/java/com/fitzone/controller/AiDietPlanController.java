package com.fitzone.controller;

import com.fitzone.model.AiDietPlanRequest;
import com.fitzone.model.AiDietPlanResponse;
import com.fitzone.model.Member;
import com.fitzone.model.MemberBmr;
import com.fitzone.repository.FileAiDietPlanRepository;
import com.fitzone.service.AiAssistantService;
import com.fitzone.service.BmrService;
import com.fitzone.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/ai-diet")
public class AiDietPlanController {

    private final AiAssistantService aiAssistantService;
    private final MemberService memberService;
    private final BmrService bmrService;
    private final FileAiDietPlanRepository aiDietPlanRepository;

    public AiDietPlanController(AiAssistantService aiAssistantService, MemberService memberService,
                                BmrService bmrService, FileAiDietPlanRepository aiDietPlanRepository) {
        this.aiAssistantService = aiAssistantService;
        this.memberService = memberService;
        this.bmrService = bmrService;
        this.aiDietPlanRepository = aiDietPlanRepository;
    }

    private Optional<Member> getLoggedInMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return memberService.getAllMembers().stream()
                .filter(m -> m.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @GetMapping("/generate")
    public String showGenerateForm(Model model, RedirectAttributes redirectAttributes) {
        Optional<Member> memberOpt = getLoggedInMember();
        if (memberOpt.isEmpty()) return "redirect:/login";

        Member member = memberOpt.get();
        Optional<MemberBmr> bmrOpt = bmrService.getLatestBmr(member.getMemberId());
        
        if (bmrOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please calculate your BMR first before generating an AI Diet Plan.");
            return "redirect:/member/bmr";
        }

        model.addAttribute("member", member);
        model.addAttribute("request", new AiDietPlanRequest());
        return "ai-diet-form";
    }

    @PostMapping("/generate")
    public String generateDietPlan(@ModelAttribute AiDietPlanRequest request, RedirectAttributes redirectAttributes) {
        Optional<Member> memberOpt = getLoggedInMember();
        if (memberOpt.isEmpty()) return "redirect:/login";

        Member member = memberOpt.get();
        Optional<MemberBmr> bmrOpt = bmrService.getLatestBmr(member.getMemberId());
        
        if (bmrOpt.isPresent()) {
            aiAssistantService.generateDietPlan(member, bmrOpt.get(), request);
            redirectAttributes.addFlashAttribute("successMessage", "AI Diet Plan generated successfully!");
            return "redirect:/ai-diet/view";
        }
        return "redirect:/member/bmr";
    }

    @GetMapping("/view")
    public String viewDietPlan(Model model) {
        Optional<Member> memberOpt = getLoggedInMember();
        if (memberOpt.isEmpty()) return "redirect:/login";

        Member member = memberOpt.get();
        Optional<AiDietPlanResponse> planOpt = aiDietPlanRepository.getAiDietPlan(member.getMemberId());
        
        if (planOpt.isEmpty()) {
            return "redirect:/ai-diet/generate";
        }

        model.addAttribute("plan", planOpt.get());
        model.addAttribute("member", member);
        return "ai-diet-view";
    }
}
