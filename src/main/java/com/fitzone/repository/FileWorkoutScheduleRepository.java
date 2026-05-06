package com.fitzone.repository;

import com.fitzone.model.WorkoutSchedule;
import com.fitzone.util.CsvUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileWorkoutScheduleRepository {

    @Value("${fitzone.data.workout-schedules-file}")
    private String workoutSchedulesFilePath;

    @PostConstruct
    public void init() {
        CsvUtil.ensureFileExists(workoutSchedulesFilePath, CsvUtil.WORKOUT_SCHEDULES_HEADER);
    }

    public void saveSchedule(WorkoutSchedule schedule) {
        CsvUtil.appendLine(workoutSchedulesFilePath, schedule.toCsvLine());
    }

    public List<WorkoutSchedule> getAllSchedules() {
        List<String> lines = CsvUtil.readDataLines(workoutSchedulesFilePath);
        List<WorkoutSchedule> schedules = new ArrayList<>();
        for (String line : lines) {
            WorkoutSchedule ws = CsvUtil.parseWorkoutScheduleLine(line);
            if (ws != null) {
                schedules.add(ws);
            }
        }
        return schedules;
    }

    public Optional<WorkoutSchedule> findById(String scheduleId) {
        return getAllSchedules().stream()
                .filter(ws -> ws.getScheduleId().equalsIgnoreCase(scheduleId))
                .findFirst();
    }
    
    public List<WorkoutSchedule> findByMemberId(String memberId) {
        return getAllSchedules().stream()
                .filter(ws -> ws.getMemberId().equalsIgnoreCase(memberId))
                .collect(Collectors.toList());
    }

    public void updateSchedule(WorkoutSchedule updatedSchedule) {
        List<WorkoutSchedule> allSchedules = getAllSchedules();
        List<String> updatedLines = new ArrayList<>();
        for (WorkoutSchedule ws : allSchedules) {
            if (ws.getScheduleId().equalsIgnoreCase(updatedSchedule.getScheduleId())) {
                updatedLines.add(updatedSchedule.toCsvLine());
            } else {
                updatedLines.add(ws.toCsvLine());
            }
        }
        CsvUtil.rewriteFile(workoutSchedulesFilePath, CsvUtil.WORKOUT_SCHEDULES_HEADER, updatedLines);
    }

    public void deleteSchedule(String scheduleId) {
        List<WorkoutSchedule> allSchedules = getAllSchedules();
        List<String> remainingLines = allSchedules.stream()
                .filter(ws -> !ws.getScheduleId().equalsIgnoreCase(scheduleId))
                .map(WorkoutSchedule::toCsvLine)
                .collect(Collectors.toList());
        CsvUtil.rewriteFile(workoutSchedulesFilePath, CsvUtil.WORKOUT_SCHEDULES_HEADER, remainingLines);
    }
}
