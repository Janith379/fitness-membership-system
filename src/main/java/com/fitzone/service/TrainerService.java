package com.fitzone.service;

import com.fitzone.model.Trainer;
import com.fitzone.repository.FileTrainerRepository;
import com.fitzone.util.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
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

    public String saveImage(MultipartFile file) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("\\s+", "_");
            Path uploadPath = Paths.get("src/main/resources/static/images/trainers/");
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not save image file: " + file.getOriginalFilename(), e);
        }
    }
}
