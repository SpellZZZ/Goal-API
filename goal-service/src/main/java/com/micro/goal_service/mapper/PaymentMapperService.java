package com.micro.goal_service.mapper;

import com.micro.goal_service.dto.PaymentCreateDto;
import com.micro.goal_service.dto.PaymentDto;
import com.micro.goal_service.model.Payment;
import com.micro.goal_service.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentMapperService {

    private final GoalService goalService;

    public Payment mapPaymentCreateDtoToPayment(PaymentCreateDto paymentDto) {

        return Payment.builder()
                .paymentValue(paymentDto.getPaymentValue())
                .goals(goalService.getGoalsByIds(paymentDto.getGoalIDs()))
                .build();
    }

    public Page<PaymentDto> mapPaymentListToPaymentDtoList(Page<Payment> payments) {
        List<PaymentDto> mappedPayments = payments.stream().map(this::mapPaymentToPaymentDto).toList();
        return new PageImpl<>(mappedPayments);
    }

    public PaymentDto mapPaymentToPaymentDto(Payment payment) {
        return  PaymentDto.builder()
                .id(payment.getId())
                .goals(payment.getGoals())
                .paymentValue(payment.getPaymentValue())
                .paymentDate(payment.getPaymentDate())
                .build();
    }

}
