package com.micro.goal_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.goal_service.PostgresContainerSetUp;
import com.micro.goal_service.exception.PriorityException;
import com.micro.goal_service.model.Priority;
import com.micro.goal_service.repo.PriorityRepository;
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
class PriorityControllerTests extends PostgresContainerSetUp {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PriorityRepository priorityRepository;

    @Test
    @Transactional
    void getPrioritiesList() throws Exception {
        int expectedListSize = priorityRepository.findAll().size();

        MvcResult mvcResult = mockMvc.perform(get("/api/priority"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        List<Priority> priorities = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Priority.class));

        Assertions.assertEquals(expectedListSize, priorities.size());
    }

    @Test
    @Transactional
    void getPriority() throws Exception {
        Priority priorityFromDB = priorityRepository.findAll().get(0);
        Long id = priorityFromDB.getId();

        MvcResult mvcResult = mockMvc.perform(get("/api/priority/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Priority requestedPriority = objectMapper.readValue(json, Priority.class);

        Assertions.assertEquals(priorityFromDB.getPriorityName(), requestedPriority.getPriorityName());
        Assertions.assertEquals(priorityFromDB.getId(), requestedPriority.getId());
    }

    @Test
    @Transactional
    void getPriorityNotExist() throws Exception {
        Long id = 123L;
        int expectedErrorMapSize = 1;
        String keyName = PriorityException.class.getSimpleName();

        MvcResult mvcResult = mockMvc.perform(get("/api/priority/{id}", id))
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
