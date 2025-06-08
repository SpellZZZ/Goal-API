package com.micro.goal_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.goal_service.PostgresContainerSetUp;
import com.micro.goal_service.util.RestResponsePage;
import com.micro.goal_service.dto.PaymentCreateDto;
import com.micro.goal_service.dto.PaymentUpdateDto;
import com.micro.goal_service.exception.PaymentException;
import com.micro.goal_service.model.Payment;
import com.micro.goal_service.repo.PaymentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PaymentControllerTests extends PostgresContainerSetUp {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PaymentRepository paymentRepository;

    @Test
    @Transactional
    void getPaymentsList() throws Exception {
        generateSamplePaymentsInDB();
        int expectedListSize = paymentRepository.findAll().size();

        MvcResult mvcResult = mockMvc.perform(get("/api/payment"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        RestResponsePage<Payment> pagePayments = objectMapper.readValue(json, RestResponsePage.class);
        List<Payment> payments = pagePayments.getContent();

        Assertions.assertEquals(expectedListSize, payments.size());
    }

    @Test
    @Transactional
    void getPayment() throws Exception {
        generateSamplePaymentsInDB();
        Payment paymentFromDB = getFirstPayment(paymentRepository.findAll());
        Long id = paymentFromDB.getId();

        MvcResult mvcResult = mockMvc.perform(get("/api/payment/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Payment requestedPayment = objectMapper.readValue(json, Payment.class);

        Assertions.assertEquals(paymentFromDB.getPaymentValue(), requestedPayment.getPaymentValue());
        Assertions.assertEquals(paymentFromDB.getId(), requestedPayment.getId());
    }

    @Test
    @Transactional
    void deletePayment() throws Exception {
        generateSamplePaymentsInDB();
        Payment paymentFromDB = getFirstPayment(paymentRepository.findAll());
        Long id = paymentFromDB.getId();

        mockMvc.perform(delete("/api/payment/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());
        Optional<Payment> optionalPayment = paymentRepository.findById(id);

        Assertions.assertFalse(optionalPayment.isPresent());
    }

    @Test
    @Transactional
    void createPayment() throws Exception {
        BigDecimal initValue=  BigDecimal.valueOf(123);
        int expectedSize = 1;

        PaymentCreateDto paymentCreateDto = PaymentCreateDto.builder()
                .paymentValue(initValue)
                .goalIDs(List.of())
                .build();
        String paymentJson = objectMapper.writeValueAsString(paymentCreateDto);

        mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paymentJson))
                .andDo(print())
                .andExpect(status().isOk());

        List<Payment> payments = paymentRepository.findAll();

        Assertions.assertEquals(expectedSize, payments.size());
        Assertions.assertEquals(0, getFirstPayment(payments).getPaymentValue().compareTo(initValue));
    }

    @Test
    @Transactional
    void putPaymentUpdate() throws Exception {
        generateSamplePaymentsInDB();
        Payment paymentToUpdate = getFirstPayment(paymentRepository.findAll());
        BigDecimal oldValue = paymentToUpdate.getPaymentValue();
        Long updateId = paymentToUpdate.getId();
        int expectedSize = paymentRepository.findAll().size();
        BigDecimal initValue =  BigDecimal.valueOf(123);

        PaymentUpdateDto paymentUpdateDto = PaymentUpdateDto.builder()
                .paymentValue(initValue)
                .goalIDs(List.of())
                .build();
        String paymentJson = objectMapper.writeValueAsString(paymentUpdateDto);

        mockMvc.perform(put("/api/payment/{id}", updateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentJson))
                .andDo(print())
                .andExpect(status().isOk());

        List<Payment> payments = paymentRepository.findAll();
        Payment updatedPayment = paymentRepository.findById(updateId).get();


        Assertions.assertEquals(expectedSize, payments.size());
        Assertions.assertNotEquals(0, oldValue.compareTo(updatedPayment.getPaymentValue()));
    }

    @Test
    @Transactional
    void putGoalException() throws Exception {
        Long updateId = 123L;
        int expectedSize = 0;
        int expectedErrorMapSize = 1;
        String keyName = PaymentException.class.getSimpleName();
        BigDecimal initValue=  BigDecimal.valueOf(123);

        PaymentUpdateDto paymentUpdateDto = PaymentUpdateDto.builder()
                .paymentValue(initValue)
                .goalIDs(List.of())
                .build();
        String goalJson = objectMapper.writeValueAsString(paymentUpdateDto);

        MvcResult mvcResult = mockMvc.perform(put("/api/payment/{id}", updateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goalJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        List<Payment> payments = paymentRepository.findAll();
        String json = mvcResult.getResponse().getContentAsString();
        Map<String, String> response = objectMapper.readValue(json, HashMap.class);

        Assertions.assertEquals(expectedSize, payments.size());
        Assertions.assertEquals(expectedErrorMapSize, response.size());
        Assertions.assertTrue(response.containsKey(keyName));
        Assertions.assertFalse(response.get(keyName).isEmpty());
    }

    @Test
    @Transactional
    void getPaymentNotExist() throws Exception {
        Long id = 123L;
        int expectedSize = 1;
        String keyName = PaymentException.class.getSimpleName();

        MvcResult mvcResult = mockMvc.perform(get("/api/payment/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Map<String, String> response = objectMapper.readValue(json, HashMap.class);

        Assertions.assertEquals(expectedSize, response.size());
        Assertions.assertTrue(response.containsKey(keyName));
        Assertions.assertFalse(response.get(keyName).isEmpty());
    }

    @Test
    @Transactional
    void paymentCreateInvalidDto() throws Exception {
        int expectedSize = 0;
        int expectedExceptionsCount = 1;
        BigDecimal invalidMoneyValue = BigDecimal.valueOf(-1);

        PaymentCreateDto paymentCreateDto = PaymentCreateDto.builder()
                .goalIDs(List.of())
                .paymentValue(invalidMoneyValue)
                .build();
        String goalJson = objectMapper.writeValueAsString(paymentCreateDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goalJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        List<Payment> payments = paymentRepository.findAll();
        Map<String, String> response = objectMapper.readValue(json, HashMap.class);

        Assertions.assertEquals(expectedSize, payments.size());
        Assertions.assertEquals(expectedExceptionsCount, response.size());
    }

    private void generateSamplePaymentsInDB() {
        Payment payment1 = Payment.builder()
                .paymentDate(LocalDateTime.now())
                .paymentValue(BigDecimal.valueOf(100))
                .build();
        Payment payment2 = Payment.builder()
                .paymentDate(LocalDateTime.now())
                .paymentValue(BigDecimal.valueOf(200))
                .build();
        Payment payment3 = Payment.builder()
                .paymentDate(LocalDateTime.now())
                .paymentValue(BigDecimal.valueOf(300))
                .build();

        paymentRepository.saveAll(List.of(payment1, payment2, payment3));
    }

    private Payment getFirstPayment(List<Payment> payments) {
        if (payments.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return payments.get(0);
        }
    }
}
