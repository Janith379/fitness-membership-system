package com.fitzone.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * Utility class to generate sequential IDs for Members (M001, M002, ...)
 * and Payments (P001, P002, ...) by reading existing CSV files.
 */
@Component
public class IdGenerator {

    @Value("${fitzone.data.members-file}")
    private String membersFilePath;

    @Value("${fitzone.data.payments-file}")
    private String paymentsFilePath;

    /**
     * Generates the next member ID by reading the members file
     * and finding the highest existing ID number.
     *
     * @return next member ID in format M001, M002, etc.
     */
    public synchronized String generateMemberId() {
        int maxNum = 0;
        try {
            Path path = Paths.get(membersFilePath);
            if (Files.exists(path)) {
                List<String> lines = Files.readAllLines(path);
                for (int i = 1; i < lines.size(); i++) {  // skip header
                    String line = lines.get(i).trim();
                    if (!line.isEmpty()) {
                        String[] parts = line.split(",", -1);
                        if (parts.length > 0 && parts[0].startsWith("M")) {
                            try {
                                int num = Integer.parseInt(parts[0].substring(1));
                                if (num > maxNum) {
                                    maxNum = num;
                                }
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading members file for ID generation: " + e.getMessage());
        }
        return String.format("M%03d", maxNum + 1);
    }

    /**
     * Generates the next payment ID by reading the payments file
     * and finding the highest existing ID number.
     *
     * @return next payment ID in format P001, P002, etc.
     */
    public synchronized String generatePaymentId() {
        int maxNum = 0;
        try {
            Path path = Paths.get(paymentsFilePath);
            if (Files.exists(path)) {
                List<String> lines = Files.readAllLines(path);
                for (int i = 1; i < lines.size(); i++) {  // skip header
                    String line = lines.get(i).trim();
                    if (!line.isEmpty()) {
                        String[] parts = line.split(",", -1);
                        if (parts.length > 0 && parts[0].startsWith("P")) {
                            try {
                                int num = Integer.parseInt(parts[0].substring(1));
                                if (num > maxNum) {
                                    maxNum = num;
                                }
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading payments file for ID generation: " + e.getMessage());
        }
        return String.format("P%03d", maxNum + 1);
    }
}
