package com.shipogle.app.controller;

import com.shipogle.app.service.PackageRequestServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class PackageRequestControllerTests {
    @InjectMocks
    PackageRequestController packageRequestController;
    @Mock
    PackageRequestServiceImpl packageRequestService;

    @Test
    public void sendRequestTest() {
        Map<String, String> req = new HashMap<>();
        req.put("driver_route_id","1");
        packageRequestController.sendPackageRequest(req);

        verify(packageRequestService,times(1)).sendRequest(req);
    }

    @Test
    public void getAllPackageRequestTest() {
        packageRequestController.getPackageRequest();

        verify(packageRequestService,times(1)).getRequest();
    }

    @Test
    public void acceptPackageRequestTest() {
        Map<String, String> req = new HashMap<>();
        req.put("package_request_id","1");
        packageRequestController.acceptPackageRequest(req);

        verify(packageRequestService,times(1)).acceptRequest(1);
    }

    @Test
    public void rejectPackageRequestTest() {
        Map<String, String> req = new HashMap<>();
        req.put("package_request_id","1");
        packageRequestController.rejectPackageRequest(req);

        verify(packageRequestService,times(1)).rejectRequest(1);
    }

    @Test
    public void deletePackageRequestTest() {
        Map<String, String> req = new HashMap<>();
        req.put("package_request_id","1");
        packageRequestController.unsendPackageRequest(req);

        verify(packageRequestService,times(1)).unsendRequest(1);
    }
}
