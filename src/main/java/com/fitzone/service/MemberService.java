package com.fitzone.service;

import com.fitzone.model.*;
import com.fitzone.repository.FileMemberRepository;
import com.fitzone.util.IdGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for Member business logic.
 * Handles member creation (with polymorphic fee calculation),
 * updates, deletion, search, and retrieval.
 */
@Service
public class MemberService {

    private final FileMemberRepository memberRepository;
    private final IdGenerator idGenerator;
    private final MembershipPlanService planService;

    public MemberService(FileMemberRepository memberRepository, IdGenerator idGenerator, MembershipPlanService planService) {
        this.memberRepository = memberRepository;
        this.idGenerator = idGenerator;
        this.planService = planService;
    }

    /**
     * Creates a new member from registration form data.
     * Uses POLYMORPHISM to create the correct subclass (RegularMember or PremiumMember)
     * and calculates the monthly fee via the overridden calculateMonthlyFee() method.
     *
     * @param form the registration form data
     * @param paymentDate the date payment was made
     * @return the created Member object with generated ID and computed dates
     */
    public Member createMember(RegistrationForm form, LocalDate paymentDate) {
        String memberId = idGenerator.generateMemberId();
        LocalDate expiryDate = paymentDate.plusMonths(form.getDurationMonths());

        // Find plan to get the fee and correct duration
        double monthlyFee = 0.0;
        Optional<MembershipPlan> planOpt = planService.getAllPlans().stream()
                .filter(p -> p.getPlanName().equalsIgnoreCase(form.getMembershipType()))
                .findFirst();
        
        if (planOpt.isPresent()) {
            monthlyFee = planOpt.get().getMonthlyFee();
        }

        Member member = new Member(
                memberId, form.getFullName(), form.getEmail(), form.getPhone(),
                form.getAge(), form.getGender(), form.getMembershipType(), form.getDurationMonths(),
                paymentDate, expiryDate, monthlyFee, form.getNotes() != null ? form.getNotes() : ""
        );

        memberRepository.saveMember(member);
        return member;
    }

    /**
     * Returns all members.
     */
    public List<Member> getAllMembers() {
        return memberRepository.getAllMembers();
    }

    /**
     * Finds a member by ID.
     */
    public Optional<Member> findById(String memberId) {
        return memberRepository.findById(memberId);
    }

    /**
     * Searches members by query (partial, case-insensitive on name, email, ID).
     */
    public List<Member> searchMembers(String query) {
        return memberRepository.searchMembers(query);
    }

    /**
     * Updates an existing member.
     * If membership type or duration changes, recalculates fee and expiry date.
     */
    public void updateMember(String memberId, RegistrationForm form) {
        Optional<Member> existing = memberRepository.findById(memberId);
        if (existing.isPresent()) {
            Member member = existing.get();

            // Always update basic info
            member.setFullName(form.getFullName());
            member.setEmail(form.getEmail());
            member.setPhone(form.getPhone());
            member.setAge(form.getAge());
            member.setGender(form.getGender());
            member.setMembershipType(form.getMembershipType());
            member.setDurationMonths(form.getDurationMonths());
            member.setNotes(form.getNotes() != null ? form.getNotes() : "");

            // Look up plan for fee
            Optional<MembershipPlan> planOpt = planService.getAllPlans().stream()
                    .filter(p -> p.getPlanName().equalsIgnoreCase(form.getMembershipType()))
                    .findFirst();
            if (planOpt.isPresent()) {
                member.setMonthlyFee(planOpt.get().getMonthlyFee());
            }

            // Recalculate expiry if duration or join date is present
            if (member.getJoinDate() != null) {
                member.setExpiryDate(member.getJoinDate().plusMonths(form.getDurationMonths()));
            }
            
            memberRepository.updateMember(member);
        }
    }

    /**
     * Deletes a member by their ID.
     */
    public void deleteMember(String memberId) {
        memberRepository.deleteMember(memberId);
    }
}
