package com.micro.goal_service.service;

import com.micro.goal_service.dto.PaymentCreateDto;
import com.micro.goal_service.dto.PaymentDto;
import com.micro.goal_service.dto.PaymentUpdateDto;
import com.micro.goal_service.exception.PaymentException;
import com.micro.goal_service.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    PaymentDto createPayment(PaymentCreateDto paymentDto);
    Payment getPaymentById(Long id) throws PaymentException;
    PaymentDto getPaymentDtoById(Long id);
    Page<Payment> getPayments(Pageable pageable);
    Page<PaymentDto> getPaymentsDto(Pageable pageable);
    void updatePayment(Long id, PaymentUpdateDto paymentUpdateDto);
    void deletePaymentById(Long id);
}
