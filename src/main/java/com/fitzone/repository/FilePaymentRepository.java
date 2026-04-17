package com.fitzone.repository;

import com.fitzone.model.Payment;
import com.fitzone.util.CsvUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//File-based repository for Payment records.
//Reads from and writes to a CSV file (payments.txt).

@Repository
public class FilePaymentRepository {

    @Value("${fitzone.data.payments-file}")
    private String paymentsFilePath;

//Ensures the payments data file exists on application startup.

    @PostConstruct
    public void init() {
        CsvUtil.ensureFileExists(paymentsFilePath, CsvUtil.PAYMENTS_HEADER);
    }

//Saves a new payment by appending to the CSV file.

    public void savePayment(Payment payment) {
        CsvUtil.appendLine(paymentsFilePath, payment.toCsvLine());
    }

//Returns all payment records from the CSV file.

    public List<Payment> getAllPayments() {
        List<String> lines = CsvUtil.readDataLines(paymentsFilePath);
        List<Payment> payments = new ArrayList<>();
        for (String line : lines) {
            Payment p = CsvUtil.parsePaymentLine(line);
            if (p != null) {
                payments.add(p);
            }
        }
        return payments;
    }

//Returns all payments for a specific member.

    public List<Payment> findByMemberId(String memberId) {
        return getAllPayments().stream()
                .filter(p -> p.getMemberId().equalsIgnoreCase(memberId))
                .collect(Collectors.toList());
    }

//Deletes all payments associated with a given member ID.
//Used when a member is deleted from the system.

    public void deleteByMemberId(String memberId) {
        List<Payment> allPayments = getAllPayments();
        List<String> remainingLines = allPayments.stream()
                .filter(p -> !p.getMemberId().equalsIgnoreCase(memberId))
                .map(Payment::toCsvLine)
                .collect(Collectors.toList());
        CsvUtil.rewriteFile(paymentsFilePath, CsvUtil.PAYMENTS_HEADER, remainingLines);
    }
}
