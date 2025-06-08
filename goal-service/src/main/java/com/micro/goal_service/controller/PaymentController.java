package com.micro.goal_service.controller;

import com.micro.goal_service.dto.PaymentCreateDto;
import com.micro.goal_service.dto.PaymentDto;
import com.micro.goal_service.dto.PaymentUpdateDto;
import com.micro.goal_service.exception.PaymentException;
import com.micro.goal_service.handler.annotation.MinValueID;
import com.micro.goal_service.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private static final String DEFAULT_SORT_FIELD = "id";
    private static final int DEFAULT_PAGE_SIZE = 20;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('user')")
    public PaymentDto createPayment(@Valid @RequestBody PaymentCreateDto paymentCreateDto) {
        log.info("Creating payment " + LocalDateTime.now());
        return paymentService.createPayment(paymentCreateDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('user')")
    public Page<PaymentDto> getPayments(
            @SortDefault(sort = DEFAULT_SORT_FIELD)
            @PageableDefault(size = DEFAULT_PAGE_SIZE)
            Pageable pageable) {
        log.info("Getting payments " + LocalDateTime.now());
        return paymentService.getPaymentsDto(pageable);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('user')")
    public PaymentDto getPayment(@MinValueID @PathVariable Long id) throws PaymentException {
        log.info("Getting payment " + LocalDateTime.now());
        return paymentService.getPaymentDtoById(id);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('user')")
    public void deletePayment(@MinValueID @PathVariable Long id) {
        log.info("Delete payment " + LocalDateTime.now());
        paymentService.deletePaymentById(id);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('user')")
    public void putPayment(@MinValueID @PathVariable Long id,
                           @Valid @RequestBody PaymentUpdateDto paymentUpdateDto) {
        log.info("Updating payment " + LocalDateTime.now());
        paymentService.updatePayment(id, paymentUpdateDto);
    }
}
