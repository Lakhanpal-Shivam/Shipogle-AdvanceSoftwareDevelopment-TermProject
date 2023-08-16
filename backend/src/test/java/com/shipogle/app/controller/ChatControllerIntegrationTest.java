package com.shipogle.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipogle.app.TestConstants;
import com.shipogle.app.model.ChatMessageRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ChatControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;


    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSendMessageIntegration() throws Exception {
        ChatMessageRequest request = new ChatMessageRequest();
        String endpoint = "/chat/";

        request.setSenderId(TestConstants.USER_ONE_ID);
        request.setReceiverId(TestConstants.USER_TWO_ID);
        request.setMessage("Hello");

        mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TestConstants.TEST_TOKEN)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
