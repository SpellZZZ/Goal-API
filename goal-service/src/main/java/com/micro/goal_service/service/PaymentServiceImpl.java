package com.micro.goal_service.service;

import com.micro.goal_service.dto.PaymentCreateDto;
import com.micro.goal_service.dto.PaymentDto;
import com.micro.goal_service.dto.PaymentUpdateDto;
import com.micro.goal_service.exception.PaymentException;
import com.micro.goal_service.model.Payment;
import com.micro.goal_service.repo.PaymentRepository;
import com.micro.goal_service.mapper.PaymentMapperService;
import com.micro.goal_service.util.DateManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final GoalService goalService;
    private final PaymentMapperService paymentMapperService;
    private final DateManager dateManager;

    @Override
    public PaymentDto createPayment(PaymentCreateDto paymentDto) {
        Payment payment = paymentMapperService.mapPaymentCreateDtoToPayment(paymentDto);

        LocalDateTime paymentDate = dateManager.dateFormat(LocalDateTime.now());
        payment.setPaymentDate(paymentDate);

        return paymentMapperService.mapPaymentToPaymentDto(paymentRepository.save(payment));
    }

    @Override
    public Payment getPaymentById(Long id) throws PaymentException {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentException("Payment doesn't exist"));
    }

    @Override
    public PaymentDto getPaymentDtoById(Long id) {
        return paymentMapperService.mapPaymentToPaymentDto(
                getPaymentById(id)
        );
    }

    @Override
    public Page<Payment> getPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }

    @Override
    public Page<PaymentDto> getPaymentsDto(Pageable pageable) {
        return paymentMapperService.mapPaymentListToPaymentDtoList(
                getPayments(pageable)
        );
    }

    @Override
    public void updatePayment(Long id, PaymentUpdateDto paymentUpdateDto) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);

        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setPaymentValue(paymentUpdateDto.getPaymentValue());
            payment.setGoals(goalService.getGoalsByIds(paymentUpdateDto.getGoalIDs()));
            paymentRepository.save(payment);
        } else {
            throw new PaymentException("Payment not found");
        }
    }

    @Override
    public void deletePaymentById(Long id) {
        paymentRepository.deleteById(id);
    }
}
