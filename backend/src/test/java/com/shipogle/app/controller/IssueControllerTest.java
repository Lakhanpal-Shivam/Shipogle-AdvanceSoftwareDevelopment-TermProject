package com.shipogle.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipogle.app.TestConstants;
import com.shipogle.app.service.IssueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class IssueControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void postIssueTest() throws Exception {
        // Given
        Map<String,String> req = new HashMap<>();
        req.put("package_order_id", "1");
        req.put("description", "Package was damaged");
        String endpoint = "/issue/post";

        mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TestConstants.TEST_TOKEN)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getIssueTest() throws Exception {
        String endpoint = "/issue/getall";

        mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TestConstants.TEST_TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
