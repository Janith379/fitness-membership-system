//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fitzone.service;

import com.fitzone.model.Payment;
import com.fitzone.model.PaymentForm;
import com.fitzone.repository.FilePaymentRepository;
import com.fitzone.util.IdGenerator;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final FilePaymentRepository paymentRepository;
    private final IdGenerator idGenerator;

    public PaymentService(FilePaymentRepository paymentRepository, IdGenerator idGenerator) {
        this.paymentRepository = paymentRepository;
        this.idGenerator = idGenerator;
    }

    public Payment processPayment(PaymentForm paymentForm, String memberId, double amount) {
        String paymentId = this.idGenerator.generatePaymentId();
        LocalDate paymentDate = LocalDate.now();
        String cardLast4 = paymentForm.getLast4Digits();
        boolean success = this.simulatePayment(paymentForm.getCardNumber());
        String status = success ? "SUCCESS" : "FAILED";
        Payment payment = new Payment(paymentId, memberId, amount, paymentDate, cardLast4, status);
        if (success) {
            this.paymentRepository.savePayment(payment);
        }

        return payment;
    }

    public void savePayment(Payment payment) {
        this.paymentRepository.savePayment(payment);
    }

    public List<Payment> getAllPayments() {
        return this.paymentRepository.getAllPayments();
    }

    public List<Payment> getPaymentsByMemberId(String memberId) {
        return this.paymentRepository.findByMemberId(memberId);
    }

    public void deletePaymentsByMemberId(String memberId) {
        this.paymentRepository.deleteByMemberId(memberId);
    }

    private boolean simulatePayment(String cardNumber) {
        if (cardNumber != null && !cardNumber.isEmpty()) {
            char firstDigit = cardNumber.charAt(0);
            return firstDigit == '4' || firstDigit == '5';
        } else {
            return false;
        }
    }
}
