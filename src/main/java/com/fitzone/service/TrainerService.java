package com.fitzone.service;

import com.fitzone.model.Trainer;
import com.fitzone.repository.FileTrainerRepository;
import com.fitzone.util.IdGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    private final FileTrainerRepository trainerRepository;
    private final IdGenerator idGenerator;

    public TrainerService(FileTrainerRepository trainerRepository, IdGenerator idGenerator) {
        this.trainerRepository = trainerRepository;
        this.idGenerator = idGenerator;
    }

    public Trainer createTrainer(Trainer trainer) {
        trainer.setTrainerId(idGenerator.generateTrainerId());
        trainerRepository.saveTrainer(trainer);
        return trainer;
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepository.getAllTrainers();
    }

    public Optional<Trainer> findById(String trainerId) {
        return trainerRepository.findById(trainerId);
    }

    public void updateTrainer(String trainerId, Trainer updatedTrainer) {
        Optional<Trainer> existing = trainerRepository.findById(trainerId);
        if (existing.isPresent()) {
            updatedTrainer.setTrainerId(trainerId);
            trainerRepository.updateTrainer(updatedTrainer);
        }
    }

    public void deleteTrainer(String trainerId) {
        trainerRepository.deleteTrainer(trainerId);
    }
}
