
package com.shipogle.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipogle.app.TestConstants;
import com.shipogle.app.model.DriverRoute;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DriverRouteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    final private int TEST_ROUTE_ID= 5678;
    final private Long ID = 1234L;
    final private int TEST_MAX_PACKAGE= 3;
    final private int TEST_MAX_WIDTH= 10;
    final private int TEST_MAX_HEIGHT= 8;
    final private int TEST_MAX_LENGTH= 12;
    final private int TEST_DAYS=3;
    final private double TEST_DATA1= 40.7128;
    final private double TEST_DATA2= -74.006;
    final private double TEST_DATA3= 41.8781;
    final private double TEST_DATA4= -87.6298;
    final private int TEST_REDIUS= 100;
    final private int TEST_PRICE= 150;

    private DriverRoute createDriverRoute(String driverId, String sourceCity, String sourceCityReferenceId,
                                          String destination, String destinationCityReferenceId, int maxPackages,
                                          int maxLength, int maxWidth, int maxHeight, Date pickupDate,
                                          Date dropoffDate, int daysToDeliver, List<Double> pickupLocationCoords,
                                          List<Double> dropoffLocationCoords, List<String> allowedCategory, int radius,
                                          int price) {
        DriverRoute driverRoute = new DriverRoute();
        driverRoute.setDriverId(driverId);
        driverRoute.setSourceCity(sourceCity);
        driverRoute.setSourceCityReferenceId(sourceCityReferenceId);
        driverRoute.setDestinationCity(destination);
        driverRoute.setDestinationCityReferenceId(destinationCityReferenceId);
        driverRoute.setMaxPackages(maxPackages);
        driverRoute.setMaxLength(maxLength);
        driverRoute.setMaxWidth(maxWidth);
        driverRoute.setMaxHeight(maxHeight);
        driverRoute.setPickupDate(pickupDate);
        driverRoute.setDropoffDate(dropoffDate);
        driverRoute.setDaysToDeliver(daysToDeliver);
        driverRoute.setPickupLocationCoords(pickupLocationCoords);
        driverRoute.setDropoffLocationCoords(dropoffLocationCoords);
        driverRoute.setAllowedCategory(allowedCategory);
        driverRoute.setRadius(radius);
        driverRoute.setPrice(price);

        return driverRoute;
    }

    @Test
    public void testCreateDriverRoute() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
       // Date date = format.parse("2023-05-01");
        DriverRoute driverRoute = createDriverRoute(
                String.valueOf(TEST_ROUTE_ID),
                "New York",
                "NYC",
                "Chicago",
                "CHI",
                TEST_MAX_PACKAGE,
                TEST_MAX_LENGTH,
                TEST_MAX_WIDTH,
                TEST_MAX_HEIGHT,
                format.parse("2023-05-01"),
                format.parse("2023-05-05"),
                TEST_DAYS,
                Arrays.asList(TEST_DATA1, TEST_DATA2),
                Arrays.asList(TEST_DATA3, TEST_DATA4),
                Arrays.asList("books", "toys"),
                TEST_REDIUS,
                TEST_PRICE
        );

        driverRoute.setDriverRouteId(ID);
        ObjectMapper objectMapper = new ObjectMapper();
        String driverRouteJson = objectMapper.writeValueAsString(driverRoute);

        mockMvc.perform(post("/driverRoutes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TestConstants.TEST_TOKEN)
                        .content(driverRouteJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetDriverRoutesByFilters() throws Exception {
        mockMvc.perform(get("/driverRoutes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("sourceCity", "New York")
                        .param("destination", "Chicago")
                        .param("pickupDataTime", "2023-05-01T08:00:00")
                        .param("maxPackages", "3")
                        .param("allowedCategory", "books,toys")
                        .param("radius", "100")
                        .param("price", "150")
                        .param("category", "books"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetDriverRoutesByDriverId() throws Exception {
        mockMvc.perform(get("/driverRoutesByDriverId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",TestConstants.TEST_TOKEN)
                        .param("driverId", "372"))
                .andExpect(status().isOk());
    }

}


