package com.shipogle.app.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

class WebConfigTests {

    @Test
    void testCorsMappings() {
        WebConfig webConfig = new WebConfig();

        CorsRegistry corsRegistry = Mockito.mock(CorsRegistry.class);
        CorsRegistration corsRegistration = Mockito.mock(CorsRegistration.class);

        Mockito.when(corsRegistry.addMapping(Mockito.any())).thenReturn(corsRegistration);
        Mockito.when(corsRegistration.allowedMethods(Mockito.any())).thenReturn(corsRegistration);
        Mockito.when(corsRegistration.allowedOrigins(Mockito.any())).thenReturn(corsRegistration);

        webConfig.addCorsMappings(corsRegistry);

        Mockito.verify(corsRegistry, Mockito.times(1)).addMapping("/**");
        Mockito.verify(corsRegistration, Mockito.times(1)).allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD");
        Mockito.verify(corsRegistration, Mockito.times(1)).allowedOrigins("*");
    }

}
