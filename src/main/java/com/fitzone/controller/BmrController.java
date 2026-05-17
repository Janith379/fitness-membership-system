package com.fitzone.controller;

import com.fitzone.model.Member;
import com.fitzone.model.MemberBmr;
import com.fitzone.service.BmrService;
import com.fitzone.service.MemberService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class BmrController {

    private final BmrService bmrService;
    private final MemberService memberService;

    public BmrController(BmrService bmrService, MemberService memberService) {
        this.bmrService = bmrService;
        this.memberService = memberService;
    }

    // --- ADMIN ENDPOINTS ---
    @GetMapping("/admin/bmr")
    public String showAdminBmr(@RequestParam(value = "memberId", required = false) String memberId, Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        
        if (memberId != null && !memberId.isEmpty()) {
            Optional<Member> memberOpt = memberService.getAllMembers().stream()
                    .filter(m -> m.getMemberId().equalsIgnoreCase(memberId))
                    .findFirst();
            if (memberOpt.isPresent()) {
                model.addAttribute("selectedMember", memberOpt.get());
                Optional<MemberBmr> bmrOpt = bmrService.getLatestBmr(memberId);
                model.addAttribute("bmr", bmrOpt.orElse(new MemberBmr()));
            }
        }
        return "admin-bmr";
    }

    @PostMapping("/admin/bmr/save")
    public String saveAdminBmr(@ModelAttribute MemberBmr bmr, RedirectAttributes redirectAttributes) {
        Optional<Member> member = memberService.getAllMembers().stream()
                .filter(m -> m.getMemberId().equalsIgnoreCase(bmr.getMemberId()))
                .findFirst();
        if (member.isPresent()) {
            bmrService.calculateAndUpdate(bmr, member.get());
            bmrService.saveBmr(bmr);
            redirectAttributes.addFlashAttribute("successMessage", "BMR Data Saved Successfully.");
        }
        return "redirect:/admin/bmr?memberId=" + bmr.getMemberId();
    }

    @GetMapping("/admin/bmr/export/{memberId}")
    public ResponseEntity<ByteArrayResource> exportBmrHistory(@PathVariable String memberId) {
        List<MemberBmr> history = bmrService.getBmrHistory(memberId);
        StringBuilder csv = new StringBuilder("Date,Height,Weight,Activity Level,Goal,BMR,Maintenance,Weight Loss,Weight Gain,Protein,Water\n");
        for (MemberBmr b : history) {
            csv.append(b.getUpdatedAt() != null ? b.getUpdatedAt().toLocalDate().toString() : "").append(",")
               .append(b.getHeight()).append(",")
               .append(b.getWeight()).append(",")
               .append(b.getActivityLevel() != null ? b.getActivityLevel() : "").append(",")
               .append(b.getFitnessGoal() != null ? b.getFitnessGoal() : "").append(",")
               .append(b.getBmr()).append(",")
               .append(b.getMaintenanceCalories()).append(",")
               .append(b.getWeightLossCalories()).append(",")
               .append(b.getWeightGainCalories()).append(",")
               .append(b.getProteinTarget()).append(",")
               .append(b.getWaterTarget()).append("\n");
        }
        ByteArrayResource resource = new ByteArrayResource(csv.toString().getBytes());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=bmr_history_" + memberId + ".csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    // --- MEMBER ENDPOINTS ---
    @GetMapping("/member/bmr")
    public String showMemberBmr(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        Optional<Member> memberOpt = memberService.getAllMembers().stream()
                .filter(m -> m.getEmail().equalsIgnoreCase(email))
                .findFirst();
                
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            model.addAttribute("member", member);
            Optional<MemberBmr> bmrOpt = bmrService.getLatestBmr(member.getMemberId());
            model.addAttribute("bmr", bmrOpt.orElse(new MemberBmr()));
            return "member-bmr";
        }
        return "redirect:/login?error";
    }

    @PostMapping("/member/bmr/save")
    public String saveMemberBmr(@ModelAttribute MemberBmr bmr, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Member> memberOpt = memberService.getAllMembers().stream()
                .filter(m -> m.getEmail().equalsIgnoreCase(email))
                .findFirst();
                
        if (memberOpt.isPresent() && memberOpt.get().getMemberId().equals(bmr.getMemberId())) {
            bmrService.calculateAndUpdate(bmr, memberOpt.get());
            bmrService.saveBmr(bmr);
            redirectAttributes.addFlashAttribute("successMessage", "BMR Data Updated Successfully.");
            return "redirect:/member/bmr";
        }
        return "redirect:/dashboard";
    }
}
