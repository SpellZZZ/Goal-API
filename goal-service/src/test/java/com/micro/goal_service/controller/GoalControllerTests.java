package com.micro.goal_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.goal_service.PostgresContainerSetUp;
import com.micro.goal_service.dto.GoalCreateDto;
import com.micro.goal_service.exception.GoalException;
import com.micro.goal_service.model.Goal;
import com.micro.goal_service.model.GoalDescription;
import com.micro.goal_service.model.Priority;
import com.micro.goal_service.repo.GoalRepository;
import com.micro.goal_service.repo.PriorityRepository;
import com.micro.goal_service.util.RestResponsePage;
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
class GoalControllerTests extends PostgresContainerSetUp {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GoalRepository goalRepository;

    @Autowired
    PriorityRepository priorityRepository;

    private Priority priorityFilterTest;

    @Test
    @Transactional
    void getGoalsList() throws Exception {
        generateSampleGoalsInDB();
        int expectedListSize = goalRepository.findAll().size();

        MvcResult mvcResult = mockMvc.perform(get("/api/goal"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        RestResponsePage<Goal> pageGoals = objectMapper.readValue(json, RestResponsePage.class);
        List<Goal> goals = pageGoals.getContent();

        Assertions.assertEquals(expectedListSize, goals.size());
    }

    @Test
    @Transactional
    void getGoal() throws Exception {
        generateSampleGoalsInDB();
        Goal goalFromDB = getFirstGoal(goalRepository.findAll());
        Long id = goalFromDB.getId();

        MvcResult mvcResult = mockMvc.perform(get("/api/goal/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Goal requestedGoal = objectMapper.readValue(json, Goal.class);

        Assertions.assertEquals(goalFromDB.getGoalName(), requestedGoal.getGoalName());
        Assertions.assertEquals(goalFromDB.getId(), requestedGoal.getId());
    }

    @Test
    @Transactional
    void deleteGoal() throws Exception {
        generateSampleGoalsInDB();
        Goal goalFromDB = getFirstGoal(goalRepository.findAll());
        Long id = goalFromDB.getId();

        mockMvc.perform(delete("/api/goal/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());
        Optional<Goal> optionalGoal = goalRepository.findById(id);

        Assertions.assertFalse(optionalGoal.isPresent());
    }

    @Test
    @Transactional
    void createGoal() throws Exception {
        BigDecimal initTargetValue =  BigDecimal.valueOf(123);
        BigDecimal initCurrentValue =  BigDecimal.valueOf(100);
        String goalName =  "sample name1";
        int expectedSize = 1;
        String goalDeadlineDate = "12.10.2025 12:00:00";

        GoalCreateDto goalCreateDto = GoalCreateDto.builder()
                .goalName(goalName)
                .dateDeadline(goalDeadlineDate)
                .currentMoney(initCurrentValue)
                .targetMoney(initTargetValue)
                .priorityID(1L)
                .periodID(1L)
                .goalDescription("Sample description")
                .build();
        String goalJson = objectMapper.writeValueAsString(goalCreateDto);

        mockMvc.perform(post("/api/goal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goalJson))
                .andDo(print())
                .andExpect(status().isOk());

        List<Goal> goals = goalRepository.findAll();

        Assertions.assertEquals(expectedSize, goals.size());
        Assertions.assertEquals(goalName, getFirstGoal(goals).getGoalName());
        Assertions.assertEquals(0, getFirstGoal(goals).getCurrentMoney().compareTo(initCurrentValue));
        Assertions.assertEquals(0, getFirstGoal(goals).getTargetMoney().compareTo(initTargetValue));
    }

    @Test
    @Transactional
    void putGoalUpdate() throws Exception {
        generateSampleGoalsInDB();
        Goal goalToUpdate = getFirstGoal(goalRepository.findAll());
        Long updateId = goalToUpdate.getId();
        int expectedSize = goalRepository.findAll().size();
        BigDecimal initTargetValue =  BigDecimal.valueOf(123);
        BigDecimal initCurrentValue =  BigDecimal.valueOf(100);
        String goalName =  "sample name1";
        String goalDeadlineDate = "12.10.2025 12:00:00";

        GoalCreateDto goalCreateDto = GoalCreateDto.builder()
                .goalName(goalName)
                .dateDeadline(goalDeadlineDate)
                .currentMoney(initCurrentValue)
                .targetMoney(initTargetValue)
                .priorityID(1L)
                .periodID(1L)
                .goalDescription("Sample description")
                .build();
        String goalJson = objectMapper.writeValueAsString(goalCreateDto);

        mockMvc.perform(put("/api/goal/{id}", updateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goalJson))
                .andDo(print())
                .andExpect(status().isOk());

        List<Goal> goals = goalRepository.findAll();
        Goal updateGoal = goalRepository.findById(updateId).get();

        Assertions.assertEquals(expectedSize, goals.size());
        Assertions.assertNotEquals(goalName, getFirstGoal(goals).getGoalName());
        Assertions.assertNotEquals(0, getFirstGoal(goals).getCurrentMoney().compareTo(updateGoal.getCurrentMoney()));
        Assertions.assertNotEquals(0, getFirstGoal(goals).getTargetMoney().compareTo(updateGoal.getTargetMoney()));
    }

    @Test
    @Transactional
    void putGoalException() throws Exception {
        Long updateId = 123L;
        int expectedSize = 0;
        int expectedErrorMapSize = 1;
        String keyName = GoalException.class.getSimpleName();
        BigDecimal initTargetValue=  BigDecimal.valueOf(123);
        BigDecimal initCurrentValue=  BigDecimal.valueOf(100);
        String goalName =  "sample name1";
        String goalDeadlineDate = "12.10.2025 12:00:00";

        GoalCreateDto goalCreateDto = GoalCreateDto.builder()
                .goalName(goalName)
                .dateDeadline(goalDeadlineDate)
                .currentMoney(initCurrentValue)
                .targetMoney(initTargetValue)
                .priorityID(1L)
                .periodID(1L)
                .goalDescription("Sample description")
                .build();
        String goalJson = objectMapper.writeValueAsString(goalCreateDto);

        MvcResult mvcResult = mockMvc.perform(put("/api/goal/{id}", updateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goalJson))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andReturn();

        List<Goal> goals = goalRepository.findAll();
        String json = mvcResult.getResponse().getContentAsString();
        Map<String, String> response = objectMapper.readValue(json, HashMap.class);

        Assertions.assertEquals(expectedSize, goals.size());
        Assertions.assertEquals(expectedErrorMapSize, response.size());
        Assertions.assertTrue(response.containsKey(keyName));
        Assertions.assertFalse(response.get(keyName).isEmpty());
    }

    @Test
    @Transactional
    void getGoalNotExist() throws Exception {
        Long id = 123L;
        int expectedSize = 1;
        String keyName = GoalException.class.getSimpleName();

        MvcResult mvcResult = mockMvc.perform(get("/api/goal/{id}", id))
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
    void goalCreateInvalidDto() throws Exception {
        int expectedSize = 0;
        int expectedExceptionsCount = 6;
        String invalidGoalName = "RND";
        String invalidDateFormat = "12/12/2012";
        BigDecimal invalidMoneyValue = BigDecimal.valueOf(-1);
        Long invalidIdValue = -1L;
        String sampleDescription = "Smaple desc";

        GoalCreateDto goalCreateDto = GoalCreateDto.builder()
                .goalName(invalidGoalName)
                .dateDeadline(invalidDateFormat)
                .targetMoney(invalidMoneyValue)
                .currentMoney(invalidMoneyValue)
                .periodID(invalidIdValue)
                .priorityID(invalidIdValue)
                .goalDescription(sampleDescription)
                .build();
        String goalJson = objectMapper.writeValueAsString(goalCreateDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/goal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goalJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        List<Goal> goals = goalRepository.findAll();
        Map<String, String> response = objectMapper.readValue(json, HashMap.class);

        Assertions.assertEquals(expectedSize, goals.size());
        Assertions.assertEquals(expectedExceptionsCount, response.size());
    }

    @Test
    @Transactional
    void goalFilterBetween() throws Exception {
        generateSampleGoalsInDB();
        String firstParam = "currentMoneyLessThan";
        String firstParamValue = "1000";
        String secondParam = "currentMoneyMoreThan";
        String secondParamValue = "300";
        int expectedResultListSize = 2;

        MvcResult mvcResult = mockMvc.perform(get("/api/goal/filter")
                        .param(firstParam, firstParamValue)
                        .param(secondParam, secondParamValue))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        RestResponsePage<Goal> pageGoals = objectMapper.readValue(json, RestResponsePage.class);
        List<Goal> goals = pageGoals.getContent();

        Assertions.assertEquals(expectedResultListSize, goals.size());
    }

    @Test
    @Transactional
    void goalFilterCompleted() throws Exception {
        generateSampleGoalsInDB();
        String firstParam = "completed";
        String firstParamValue = "true";
        int expectedResultListSize = 1;

        MvcResult mvcResult = mockMvc.perform(get("/api/goal/filter")
                        .param(firstParam, firstParamValue))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        RestResponsePage<Goal> pageGoals = objectMapper.readValue(json, RestResponsePage.class);
        List<Goal> goals = pageGoals.getContent();

        Assertions.assertEquals(expectedResultListSize, goals.size());
    }

    @Test
    @Transactional
    void goalFilterByPriority() throws Exception {
        generateSampleGoalsInDB();
        String firstParam = "priorityName";
        String firstParamValue = priorityFilterTest.getPriorityName();
        int expectedResultListSize = 2;

        MvcResult mvcResult = mockMvc.perform(get("/api/goal/filter")
                        .param(firstParam, firstParamValue))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        RestResponsePage<Goal> pageGoals = objectMapper.readValue(json, RestResponsePage.class);
        List<Goal> goals = pageGoals.getContent();

        Assertions.assertEquals(expectedResultListSize, goals.size());
    }

    private void generateSampleGoalsInDB() {
        priorityFilterTest = priorityRepository.findAll().get(0);

        GoalDescription goalDescription1 = new GoalDescription();
        GoalDescription goalDescription2 = new GoalDescription();
        GoalDescription goalDescription3 = new GoalDescription();
        goalDescription1.setDescription("sample1");
        goalDescription1.setDescription("sample2");
        goalDescription1.setDescription("sample3");

        Goal goal1 = Goal.builder()
                .goalName("goal1")
                .currentMoney(BigDecimal.valueOf(100))
                .targetMoney(BigDecimal.valueOf(200))
                .goalDescription(goalDescription1)
                .priority(priorityFilterTest)
                .isCompleted(true)
                .build();
        Goal goal2 = Goal.builder()
                .goalName("goal2")
                .currentMoney(BigDecimal.valueOf(400))
                .targetMoney(BigDecimal.valueOf(500))
                .goalDescription(goalDescription2)
                .priority(priorityFilterTest)
                .build();
        Goal goal3 = Goal.builder()
                .goalName("goal3")
                .currentMoney(BigDecimal.valueOf(600))
                .targetMoney(BigDecimal.valueOf(700))
                .goalDescription(goalDescription3)
                .build();
        goalRepository.saveAll(List.of(goal1, goal2, goal3));
    }

    private Goal getFirstGoal(List<Goal> goals) {
        if (goals.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return goals.get(0);
        }
    }
}
