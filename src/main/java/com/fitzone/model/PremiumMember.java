package com.fitzone.model;

import java.time.LocalDate;

/**
 * OOP CONCEPT — INHERITANCE: PremiumMember extends the abstract Member class.
 * OOP CONCEPT — POLYMORPHISM: Overrides calculateMonthlyFee() to return
 *   the Premium membership fee (LKR 7,500).
 */
public class PremiumMember extends Member {

    private static final double PREMIUM_MONTHLY_FEE = 7500.00;

    public PremiumMember() {
        super();
        setMembershipType("Premium");
    }

    public PremiumMember(String memberId, String fullName, String email, String phone,
                         int age, String gender, int durationMonths,
                         LocalDate joinDate, LocalDate expiryDate, String notes) {
        super(memberId, fullName, email, phone, age, gender, "Premium",
                durationMonths, joinDate, expiryDate, PREMIUM_MONTHLY_FEE, notes);
    }


     //POLYMORPHISM — Returns the monthly fee for Premium members.
     //kjnj
    @Override
    public double calculateMonthlyFee() {
        return PREMIUM_MONTHLY_FEE;
    }
}
