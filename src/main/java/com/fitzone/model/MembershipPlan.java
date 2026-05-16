package com.fitzone.model;

public class MembershipPlan {
    private String planId;
    private String planName;
    private double monthlyFee;
    private String description;
    private int durationMonths;
    private String targetGender; // Male, Female, Any

    public MembershipPlan() {}

    public MembershipPlan(String planId, String planName, double monthlyFee, String description, int durationMonths, String targetGender) {
        this.planId = planId;
        this.planName = planName;
        this.monthlyFee = monthlyFee;
        this.description = description;
        this.durationMonths = durationMonths;
        this.targetGender = targetGender;
    }

    public String getPlanId() { return planId; }
    public void setPlanId(String planId) { this.planId = planId; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public double getMonthlyFee() { return monthlyFee; }
    public void setMonthlyFee(double monthlyFee) { this.monthlyFee = monthlyFee; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDurationMonths() { return durationMonths; }
    public void setDurationMonths(int durationMonths) { this.durationMonths = durationMonths; }

    public String getTargetGender() { return targetGender; }
    public void setTargetGender(String targetGender) { this.targetGender = targetGender; }

    public String toCsvLine() {
        return String.join(",",
                safe(planId),
                safe(planName),
                String.format("%.2f", monthlyFee),
                safe(description).replace(",", ";").replace("\n", " ").replace("\r", " "),
                String.valueOf(durationMonths),
                safe(targetGender)
        );
    }

    private String safe(String value) {
        return value != null ? value : "";
    }
}
