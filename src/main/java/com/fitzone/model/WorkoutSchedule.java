package com.fitzone.model;

public class WorkoutSchedule {
    private String scheduleId;
    private String memberId;
    private String trainerId;
    private String dayOfWeek;
    private String workoutDetails;

    public WorkoutSchedule() {}

    public WorkoutSchedule(String scheduleId, String memberId, String trainerId, String dayOfWeek, String workoutDetails) {
        this.scheduleId = scheduleId;
        this.memberId = memberId;
        this.trainerId = trainerId;
        this.dayOfWeek = dayOfWeek;
        this.workoutDetails = workoutDetails;
    }

    public String getScheduleId() { return scheduleId; }
    public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getTrainerId() { return trainerId; }
    public void setTrainerId(String trainerId) { this.trainerId = trainerId; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public String getWorkoutDetails() { return workoutDetails; }
    public void setWorkoutDetails(String workoutDetails) { this.workoutDetails = workoutDetails; }

    public String toCsvLine() {
        return String.join(",",
                safe(scheduleId),
                safe(memberId),
                safe(trainerId),
                safe(dayOfWeek),
                safe(workoutDetails)
        );
    }

    private String safe(String value) {
        return value != null ? value : "";
    }
}
