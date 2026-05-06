package com.fitzone.service;

import com.fitzone.model.WorkoutSchedule;
import com.fitzone.repository.FileWorkoutScheduleRepository;
import com.fitzone.util.IdGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutScheduleService {

    private final FileWorkoutScheduleRepository scheduleRepository;
    private final IdGenerator idGenerator;

    public WorkoutScheduleService(FileWorkoutScheduleRepository scheduleRepository, IdGenerator idGenerator) {
        this.scheduleRepository = scheduleRepository;
        this.idGenerator = idGenerator;
    }

    public WorkoutSchedule createSchedule(WorkoutSchedule schedule) {
        schedule.setScheduleId(idGenerator.generateWorkoutId());
        scheduleRepository.saveSchedule(schedule);
        return schedule;
    }

    public List<WorkoutSchedule> getAllSchedules() {
        return scheduleRepository.getAllSchedules();
    }

    public Optional<WorkoutSchedule> findById(String scheduleId) {
        return scheduleRepository.findById(scheduleId);
    }
    
    public List<WorkoutSchedule> findByMemberId(String memberId) {
        return scheduleRepository.findByMemberId(memberId);
    }

    public void updateSchedule(String scheduleId, WorkoutSchedule updatedSchedule) {
        Optional<WorkoutSchedule> existing = scheduleRepository.findById(scheduleId);
        if (existing.isPresent()) {
            updatedSchedule.setScheduleId(scheduleId);
            scheduleRepository.updateSchedule(updatedSchedule);
        }
    }

    public void deleteSchedule(String scheduleId) {
        scheduleRepository.deleteSchedule(scheduleId);
    }
}
