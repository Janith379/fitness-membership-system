package com.fitzone.model;

import java.time.LocalDate;

/**
 * OOP CONCEPT — INHERITANCE: RegularMember extends the abstract Member class.
 * OOP CONCEPT — POLYMORPHISM: Overrides calculateMonthlyFee() to return
 *   the Regular membership fee (LKR 5,000).
 */
public class RegularMember extends Member {

    private static final double REGULAR_MONTHLY_FEE = 5000.00;

    public RegularMember() {
        super();
        setMembershipType("Regular");
    }

    public RegularMember(String memberId, String fullName, String email, String phone,
                         int age, String gender, int durationMonths,
                         LocalDate joinDate, LocalDate expiryDate, String notes) {
        super(memberId, fullName, email, phone, age, gender, "Regular",
                durationMonths, joinDate, expiryDate, REGULAR_MONTHLY_FEE, notes);
    }

    /**
     * POLYMORPHISM — Returns the monthly fee for Regular members.
     */
    @Override
    public double calculateMonthlyFee() {
        return REGULAR_MONTHLY_FEE;
    }
}
