package com.micro.goal_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.goal_service.PostgresContainerSetUp;
import com.micro.goal_service.exception.PeriodException;
import com.micro.goal_service.model.Period;
import com.micro.goal_service.repo.PeriodRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PeriodControllerTests extends PostgresContainerSetUp {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PeriodRepository periodRepository;

    @Test
    @Transactional
    void getPeriodsList() throws Exception {
        int expectedListSize = periodRepository.findAll().size();

        MvcResult mvcResult = mockMvc.perform(get("/api/period"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        List<Period> periods = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Period.class));

        Assertions.assertEquals(expectedListSize, periods.size());
    }

    @Test
    @Transactional
    void getPeriod() throws Exception {
        Period periodFromDB = periodRepository.findAll().get(0);
        Long id = periodFromDB.getId();

        MvcResult mvcResult = mockMvc.perform(get("/api/period/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Period requestedPriority = objectMapper.readValue(json, Period.class);

        Assertions.assertEquals(periodFromDB.getPeriodName(), requestedPriority.getPeriodName());
        Assertions.assertEquals(periodFromDB.getId(), requestedPriority.getId());
    }

    @Test
    @Transactional
    void getPeriodNotExist() throws Exception {
        Long id = 123L;
        int expectedErrorMapSize = 1;
        String keyName = PeriodException.class.getSimpleName();

        MvcResult mvcResult = mockMvc.perform(get("/api/period/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Map<String, String> response = objectMapper.readValue(json, HashMap.class);

        Assertions.assertEquals(expectedErrorMapSize, response.size());
        Assertions.assertTrue(response.containsKey(keyName));
        Assertions.assertFalse(response.get(keyName).isEmpty());
    }
}
