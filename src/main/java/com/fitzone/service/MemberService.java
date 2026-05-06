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

    public MemberService(FileMemberRepository memberRepository, IdGenerator idGenerator) {
        this.memberRepository = memberRepository;
        this.idGenerator = idGenerator;
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

        // POLYMORPHISM — Create the correct subclass based on membership type
        Member member;
        if ("Premium".equalsIgnoreCase(form.getMembershipType())) {
            member = new PremiumMember(
                    memberId, form.getFullName(), form.getEmail(), form.getPhone(),
                    form.getAge(), form.getGender(), form.getDurationMonths(),
                    paymentDate, expiryDate, form.getNotes() != null ? form.getNotes() : ""
            );
        } else {
            member = new RegularMember(
                    memberId, form.getFullName(), form.getEmail(), form.getPhone(),
                    form.getAge(), form.getGender(), form.getDurationMonths(),
                    paymentDate, expiryDate, form.getNotes() != null ? form.getNotes() : ""
            );
        }

        // The monthlyFee is set by the constructor via calculateMonthlyFee()
        member.setMonthlyFee(member.calculateMonthlyFee());

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

            // If membership type changed, create a new instance of the correct type
            boolean typeChanged = !member.getMembershipType().equalsIgnoreCase(form.getMembershipType());

            if (typeChanged) {
                Member newMember;
                if ("Premium".equalsIgnoreCase(form.getMembershipType())) {
                    newMember = new PremiumMember();
                } else {
                    newMember = new RegularMember();
                }
                newMember.setMemberId(memberId);
                newMember.setJoinDate(member.getJoinDate());
                newMember.setMonthlyFee(newMember.calculateMonthlyFee());
                newMember.setFullName(form.getFullName());
                newMember.setEmail(form.getEmail());
                newMember.setPhone(form.getPhone());
                newMember.setAge(form.getAge());
                newMember.setGender(form.getGender());
                newMember.setMembershipType(form.getMembershipType());
                newMember.setDurationMonths(form.getDurationMonths());
                newMember.setNotes(form.getNotes() != null ? form.getNotes() : "");
                // Recalculate expiry based on join date and new duration
                if (newMember.getJoinDate() != null) {
                    newMember.setExpiryDate(newMember.getJoinDate().plusMonths(form.getDurationMonths()));
                }
                memberRepository.updateMember(newMember);
            } else {
                member.setFullName(form.getFullName());
                member.setEmail(form.getEmail());
                member.setPhone(form.getPhone());
                member.setAge(form.getAge());
                member.setGender(form.getGender());
                member.setDurationMonths(form.getDurationMonths());
                member.setNotes(form.getNotes() != null ? form.getNotes() : "");
                // Recalculate expiry if duration changed
                if (member.getJoinDate() != null) {
                    member.setExpiryDate(member.getJoinDate().plusMonths(form.getDurationMonths()));
                }
                memberRepository.updateMember(member);
            }
        }
    }

    /**
     * Deletes a member by their ID.
     */
    public void deleteMember(String memberId) {
        memberRepository.deleteMember(memberId);
    }
}
