package com.fitzone.repository;

import com.fitzone.model.Trainer;
import com.fitzone.util.CsvUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileTrainerRepository {

    @Value("${fitzone.data.trainers-file}")
    private String trainersFilePath;

    @PostConstruct
    public void init() {
        CsvUtil.ensureFileExists(trainersFilePath, CsvUtil.TRAINERS_HEADER);
    }

    public void saveTrainer(Trainer trainer) {
        CsvUtil.appendLine(trainersFilePath, trainer.toCsvLine());
    }

    public List<Trainer> getAllTrainers() {
        List<String> lines = CsvUtil.readDataLines(trainersFilePath);
        List<Trainer> trainers = new ArrayList<>();
        for (String line : lines) {
            Trainer t = CsvUtil.parseTrainerLine(line);
            if (t != null) {
                trainers.add(t);
            }
        }
        return trainers;
    }

    public Optional<Trainer> findById(String trainerId) {
        return getAllTrainers().stream()
                .filter(t -> t.getTrainerId().equalsIgnoreCase(trainerId))
                .findFirst();
    }

    public void updateTrainer(Trainer updatedTrainer) {
        List<Trainer> allTrainers = getAllTrainers();
        List<String> updatedLines = new ArrayList<>();
        for (Trainer t : allTrainers) {
            if (t.getTrainerId().equalsIgnoreCase(updatedTrainer.getTrainerId())) {
                updatedLines.add(updatedTrainer.toCsvLine());
            } else {
                updatedLines.add(t.toCsvLine());
            }
        }
        CsvUtil.rewriteFile(trainersFilePath, CsvUtil.TRAINERS_HEADER, updatedLines);
    }

    public void deleteTrainer(String trainerId) {
        List<Trainer> allTrainers = getAllTrainers();
        List<String> remainingLines = allTrainers.stream()
                .filter(t -> !t.getTrainerId().equalsIgnoreCase(trainerId))
                .map(Trainer::toCsvLine)
                .collect(Collectors.toList());
        CsvUtil.rewriteFile(trainersFilePath, CsvUtil.TRAINERS_HEADER, remainingLines);
    }
}
