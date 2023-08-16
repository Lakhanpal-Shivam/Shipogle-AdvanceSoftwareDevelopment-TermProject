package com.shipogle.app.controller;

import com.shipogle.app.TestConstants;
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
public class RatingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void postRatingTest() throws Exception {
        String endpoint = "/rating/post";
        String req = "{\"driver_route_id\": 25,\"star\": 4.5,\"review\": \"Test review of driver\"}";


        mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TestConstants.TEST_TOKEN)
                        .content(req))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getDelivererRatingTest() throws Exception {
        String endpoint = "/rating/deliverer/getall";


        mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TestConstants.TEST_TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getSenderPostedRatingTest() throws Exception {
        String endpoint = "/rating/posted/getall";


        mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TestConstants.TEST_TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getDelivererRatingByIDTest() throws Exception {
        String endpoint = "/rating/deliverer?driver_id=40";

        mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TestConstants.TEST_TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
