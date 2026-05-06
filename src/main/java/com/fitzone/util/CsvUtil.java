package com.fitzone.util;

import com.fitzone.model.*;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading and writing CSV data files.
 * Provides thread-safe file I/O operations for members and payments.
 */
public final class CsvUtil {

    // CSV Headers
    public static final String MEMBERS_HEADER =
            "memberId,fullName,email,phone,age,gender,membershipType,durationMonths,joinDate,expiryDate,monthlyFee,notes";

    public static final String PAYMENTS_HEADER =
            "paymentId,memberId,amount,paymentDate,cardLast4,status";

    public static final String TRAINERS_HEADER =
            "trainerId,fullName,specialization,phone,email";

    public static final String MEMBERSHIP_PLANS_HEADER =
            "planId,planName,monthlyFee,description,durationMonths,targetGender";

    public static final String WORKOUT_SCHEDULES_HEADER =
            "scheduleId,memberId,trainerId,dayOfWeek,workoutDetails";

    public static final String DIET_PLANS_HEADER =
            "dietId,memberId,mealType,foodItems,calories";

    private CsvUtil() {
        // Utility class — prevent instantiation
    }

    /**
     * Converts a file path string into a Path safely.
     */
    private static Path toPath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("CSV file path must not be null or blank.");
        }
        return Paths.get(filePath.trim());
    }

    /**
     * Ensures the data file exists with its header line.
     * Creates parent directories and the file if they don't exist.
     */
    public static synchronized void ensureFileExists(String filePath, String header) {
        try {
            Path path = toPath(filePath);
            Path parent = path.getParent();

            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            if (!Files.exists(path)) {
                Files.writeString(
                        path,
                        header + System.lineSeparator(),
                        StandardOpenOption.CREATE_NEW
                );
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error ensuring file exists: " + filePath + " — " + e.getMessage());
        }
    }

    /**
     * Parses a CSV line into a Member object (RegularMember or PremiumMember).
     * Returns null if the line is malformed.
     */
    public static Member parseMemberLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",", -1);

        if (parts.length < 11) {
            return null;
        }

        try {
            String memberId = parts[0].trim();
            String fullName = parts[1].trim();
            String email = parts[2].trim();
            String phone = parts[3].trim();
            int age = Integer.parseInt(parts[4].trim());
            String gender = parts[5].trim();
            String membershipType = parts[6].trim();
            int durationMonths = Integer.parseInt(parts[7].trim());
            LocalDate joinDate = parts[8].trim().isEmpty() ? null : LocalDate.parse(parts[8].trim());
            LocalDate expiryDate = parts[9].trim().isEmpty() ? null : LocalDate.parse(parts[9].trim());
            double monthlyFee = parts[10].trim().isEmpty() ? 0.0 : Double.parseDouble(parts[10].trim());
            String notes = parts.length > 11 ? parts[11].trim() : "";

            return new Member(
                    memberId,
                    fullName,
                    email,
                    phone,
                    age,
                    gender,
                    membershipType,
                    durationMonths,
                    joinDate,
                    expiryDate,
                    monthlyFee,
                    notes
            );
        } catch (NumberFormatException | DateTimeParseException e) {
            System.err.println("Error parsing member line: " + line + " — " + e.getMessage());
            return null;
        }
    }

    /**
     * Parses a CSV line into a Payment object.
     * Returns null if the line is malformed.
     */
    public static Payment parsePaymentLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",", -1);

        if (parts.length < 6) {
            return null;
        }

        try {
            String paymentId = parts[0].trim();
            String memberId = parts[1].trim();
            double amount = Double.parseDouble(parts[2].trim());
            LocalDate paymentDate = parts[3].trim().isEmpty() ? null : LocalDate.parse(parts[3].trim());
            String cardLast4 = parts[4].trim();
            String status = parts[5].trim();

            return new Payment(paymentId, memberId, amount, paymentDate, cardLast4, status);
        } catch (NumberFormatException | DateTimeParseException e) {
            System.err.println("Error parsing payment line: " + line + " — " + e.getMessage());
            return null;
        }
    }

    public static Trainer parseTrainerLine(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] parts = line.split(",", -1);
        if (parts.length < 5) return null;
        return new Trainer(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim());
    }

    public static MembershipPlan parseMembershipPlanLine(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] parts = line.split(",", -1);
        try {
            return new MembershipPlan(
                    parts[0].trim(),
                    parts[1].trim(),
                    Double.parseDouble(parts[2].trim()),
                    parts[3].trim(),
                    Integer.parseInt(parts[4].trim()),
                    parts.length > 5 ? parts[5].trim() : "Any"
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static WorkoutSchedule parseWorkoutScheduleLine(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] parts = line.split(",", -1);
        if (parts.length < 5) return null;
        return new WorkoutSchedule(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim());
    }

    public static DietPlan parseDietPlanLine(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] parts = line.split(",", -1);
        if (parts.length < 5) return null;
        try {
            return new DietPlan(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), Integer.parseInt(parts[4].trim()));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Reads all lines from a file, skipping the header.
     * Returns an empty list if the file doesn't exist.
     */
    public static List<String> readDataLines(String filePath) {
        List<String> dataLines = new ArrayList<>();

        try {
            Path path = toPath(filePath);

            if (Files.exists(path)) {
                List<String> allLines = Files.readAllLines(path);

                for (int i = 1; i < allLines.size(); i++) {
                    String line = allLines.get(i).trim();

                    if (!line.isEmpty()) {
                        dataLines.add(line);
                    }
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error reading file: " + filePath + " — " + e.getMessage());
        }

        return dataLines;
    }

    /**
     * Appends a single line to the end of a CSV file (thread-safe).
     */
    public static synchronized void appendLine(String filePath, String line) {
        try {
            Path path = toPath(filePath);

            Files.writeString(
                    path,
                    line + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error appending to file: " + filePath + " — " + e.getMessage());
        }
    }

    /**
     * Rewrites the entire CSV file with the given header and data lines (thread-safe).
     * Used for update and delete operations.
     */
    public static synchronized void rewriteFile(String filePath, String header, List<String> dataLines) {
        try {
            Path path = toPath(filePath);

            StringBuilder sb = new StringBuilder();
            sb.append(header).append(System.lineSeparator());

            for (String line : dataLines) {
                sb.append(line).append(System.lineSeparator());
            }

            Files.writeString(
                    path,
                    sb.toString(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error rewriting file: " + filePath + " — " + e.getMessage());
        }
    }
}