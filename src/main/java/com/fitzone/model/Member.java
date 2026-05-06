package com.fitzone.model;

import java.time.LocalDate;

/**
 * Abstract base class for all gym members.
 * OOP CONCEPT — ENCAPSULATION: All fields are private with public getters/setters.
 * OOP CONCEPT — ABSTRACTION: This class is abstract; it cannot be instantiated directly.
 * OOP CONCEPT — POLYMORPHISM: The abstract method calculateMonthlyFee() is overridden
 *   by RegularMember and PremiumMember to return different fee amounts.
 * OOP CONCEPT — INHERITANCE: RegularMember and PremiumMember extend this class.
 */
public abstract class Member {

    // --- Private fields (ENCAPSULATION) ---
    private String memberId;
    private String fullName;
    private String email;
    private String phone;
    private int age;
    private String gender;
    private String membershipType;   // "Regular" or "Premium"
    private int durationMonths;
    private LocalDate joinDate;
    private LocalDate expiryDate;
    private double monthlyFee;
    private String notes;

    // --- Constructors ---
    protected Member() {
    }

    protected Member(String memberId, String fullName, String email, String phone,
                     int age, String gender, String membershipType, int durationMonths,
                     LocalDate joinDate, LocalDate expiryDate, double monthlyFee, String notes) {
        this.memberId = memberId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
        this.membershipType = membershipType;
        this.durationMonths = durationMonths;
        this.joinDate = joinDate;
        this.expiryDate = expiryDate;
        this.monthlyFee = monthlyFee;
        this.notes = notes;
    }

    /**
     * POLYMORPHISM — Abstract method overridden by subclasses to compute
     * the monthly membership fee based on membership type.
     */
    public abstract double calculateMonthlyFee();

    /**
     * Calculates the total payment amount = monthlyFee * durationMonths.
     */
    public double calculateTotalAmount() {
        return calculateMonthlyFee() * durationMonths;
    }

    // --- Getters and Setters (ENCAPSULATION) ---

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Converts this member to a CSV line for file storage.
     */
    public String toCsvLine() {
        return String.join(",",
                safe(memberId),
                safe(fullName),
                safe(email),
                safe(phone),
                String.valueOf(age),
                safe(gender),
                safe(membershipType),
                String.valueOf(durationMonths),
                joinDate != null ? joinDate.toString() : "",
                expiryDate != null ? expiryDate.toString() : "",
                String.format("%.2f", monthlyFee),
                safe(notes)
        );
    }

    private String safe(String value) {
        return value != null ? value : "";
    }

    @Override
    public String toString() {
        return membershipType + " Member [" + memberId + "] " + fullName;
    }
}
