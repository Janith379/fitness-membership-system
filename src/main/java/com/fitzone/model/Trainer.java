package com.fitzone.model;

public class Trainer {
    private String trainerId;
    private String fullName;
    private String specialization;
    private String phone;
    private String email;

    public Trainer() {}

    public Trainer(String trainerId, String fullName, String specialization, String phone, String email) {
        this.trainerId = trainerId;
        this.fullName = fullName;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
    }

    public String getTrainerId() { return trainerId; }
    public void setTrainerId(String trainerId) { this.trainerId = trainerId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String toCsvLine() {
        return String.join(",",
                safe(trainerId),
                safe(fullName),
                safe(specialization),
                safe(phone),
                safe(email)
        );
    }

    private String safe(String value) {
        return value != null ? value : "";
    }
}
