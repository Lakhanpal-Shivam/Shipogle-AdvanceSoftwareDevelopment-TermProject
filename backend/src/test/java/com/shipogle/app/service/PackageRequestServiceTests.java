package com.shipogle.app.service;

import com.shipogle.app.model.DriverRoute;
import com.shipogle.app.model.Package;
import com.shipogle.app.model.PackageRequest;
import com.shipogle.app.model.User;
import com.shipogle.app.repository.DriverRouteRepository;
import com.shipogle.app.repository.PackageRepository;
import com.shipogle.app.repository.PackageRequestRepository;
import com.shipogle.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PackageRequestServiceTests {
    @InjectMocks
    PackageRequestServiceImpl packageRequestService;
    @Mock
    PackageRequest packageRequest;
    @Mock
    PackageRequestRepository packageRequestRepo;
    @Mock
    User user;
    @Mock
    UserRepository userRepo;
    @Mock
    UserServiceImpl userService;
    @Mock
    PackageOrderServiceImpl packageOrderService;
    @Mock
    DriverRouteRepository driverRouteRepo;
    @Mock
    DriverRoute driverRoute;
    @Mock
    Package _package;
    @Mock
    PackageRepository packageRepo;
    private final int TEST_PACKAGE_ID = 20;

    @Test
    public void sendRequestTestOrderAlreadyExist(){
        Map<String,String> req = new HashMap<>();
        req.put("package_id","20");
        req.put("driver_route_id","1");
        Mockito.when(packageRequestRepo.countAllBy_package_IdAndDriverRoute_Id(TEST_PACKAGE_ID, 1L)).thenReturn(0);
        Mockito.when(packageOrderService.isPackageOrderExist(TEST_PACKAGE_ID)).thenReturn(true);

        assertThrows(ResponseStatusException.class,()->packageRequestService.sendRequest(req));
    }

    @Test
    public void sendRequestTestRequestAlreadyExist(){
        Map<String,String> req = new HashMap<>();
        req.put("package_id","20");
        req.put("driver_route_id","1");
        Mockito.when(packageRequestRepo.countAllBy_package_IdAndDriverRoute_Id(TEST_PACKAGE_ID, 1L)).thenReturn(1);
        Mockito.when(packageOrderService.isPackageOrderExist(TEST_PACKAGE_ID)).thenReturn(false);

        assertThrows(ResponseStatusException.class,()->packageRequestService.sendRequest(req));
    }

    @Test
    public void sendRequestTestExceptionInvalidUser(){
        Map<String,String> req = new HashMap<>();
        req.put("package_id","20");
        req.put("driver_route_id","1");
        Mockito.when(packageRequestRepo.countAllBy_package_IdAndDriverRoute_Id(TEST_PACKAGE_ID, 1L)).thenReturn(0);
        Mockito.when(packageOrderService.isPackageOrderExist(TEST_PACKAGE_ID)).thenReturn(false);

        Mockito.when(driverRouteRepo.getDriverRouteById(Long.valueOf(1))).thenReturn(null);

        assertThrows(ResponseStatusException.class,()->packageRequestService.sendRequest(req));
    }

    @Test
    public void sendRequestTestExceptionSuccess(){
        Map<String,String> req = new HashMap<>();
        req.put("package_id","20");
        req.put("driver_route_id","1");
        req.put("ask_price","100");

        Mockito.when(packageRequestRepo.countAllBy_package_IdAndDriverRoute_Id(Mockito.any(),Mockito.any())).thenReturn(0);
        Mockito.when(packageOrderService.isPackageOrderExist(Mockito.any())).thenReturn(false);
        Mockito.when(userService.getLoggedInUser()).thenReturn(user);
        Mockito.when(driverRouteRepo.getDriverRouteById(Mockito.any())).thenReturn(driverRoute);
        Mockito.when(driverRoute.getDriverId()).thenReturn(String.valueOf(1L));
        Mockito.when(userRepo.getUserById(Mockito.any())).thenReturn(user);
        Mockito.when(packageRepo.getPackageById(Mockito.any())).thenReturn(_package);
        assertEquals("Request sent",packageRequestService.sendRequest(req));
    }

    @Test
    public void acceptRequestTestRejectedRequest(){
        Mockito.when(packageRequestRepo.getPackageRequestById(1)).thenReturn(packageRequest);
        Mockito.when(packageRequest.getStatus()).thenReturn("rejected");

        assertThrows(ResponseStatusException.class,()->packageRequestService.acceptRequest(1));
    }

    @Test
    public void acceptRequestTestAcceptedRequest(){
        Mockito.when(packageRequestRepo.getPackageRequestById(1)).thenReturn(packageRequest);
        Mockito.when(packageRequest.getStatus()).thenReturn("accepted");

        assertThrows(ResponseStatusException.class,()->packageRequestService.acceptRequest(1));
    }

    @Test
    public void acceptRequestTestSuccess(){
        List<PackageRequest> requests = new ArrayList<>();

        Mockito.when(packageRequestRepo.getPackageRequestById(1)).thenReturn(packageRequest);
        Mockito.when(packageRequest.getStatus()).thenReturn("pending");
        Mockito.when(packageRequest.get_package()).thenReturn(_package);
        Mockito.when(_package.getId()).thenReturn(TEST_PACKAGE_ID);
        Mockito.when(packageRequestRepo.getAllBy_package_Id(TEST_PACKAGE_ID)).thenReturn(requests);

        Mockito.when(packageOrderService.createPackageOrder(packageRequest)).thenReturn("order created");

        assertEquals("Request accepted",packageRequestService.acceptRequest(1));

    }

    @Test
    public void acceptRequestTestUnableToCreateOrder(){
        List<PackageRequest> requests = new ArrayList<>();

        Mockito.when(packageRequestRepo.getPackageRequestById(1)).thenReturn(packageRequest);
        Mockito.when(packageRequest.getStatus()).thenReturn("pending");
        Mockito.when(packageRequest.get_package()).thenReturn(_package);
        Mockito.when(_package.getId()).thenReturn(TEST_PACKAGE_ID);
        Mockito.when(packageRequestRepo.getAllBy_package_Id(TEST_PACKAGE_ID)).thenReturn(requests);

        Mockito.when(packageOrderService.createPackageOrder(packageRequest)).thenReturn("order not created");

        assertThrows(ResponseStatusException.class,()->packageRequestService.acceptRequest(1));

    }

    @Test
    public void rejectRequestTestAlreadyRejected(){
        Mockito.when(packageRequestRepo.getPackageRequestById(1)).thenReturn(packageRequest);
        Mockito.when(packageRequest.getStatus()).thenReturn("rejected");

        assertThrows(ResponseStatusException.class,() -> packageRequestService.rejectRequest(1));

    }

    @Test
    public void rejectRequestTestSuccess(){
        Mockito.when(packageRequestRepo.getPackageRequestById(1)).thenReturn(packageRequest);
        Mockito.when(packageRequest.getStatus()).thenReturn("pending");

        assertEquals("Request rejected",packageRequestService.rejectRequest(1));

    }

    @Test
    public void unSendRequestTestRequestNotFound(){
        Mockito.when(packageRequestRepo.getPackageRequestById(1)).thenReturn(null);
        assertThrows(ResponseStatusException.class,()->packageRequestService.unsendRequest(1));
    }

    @Test
    public void unSendRequestTestAcceptedRequest(){
        Mockito.when(packageRequestRepo.getPackageRequestById(1)).thenReturn(packageRequest);
        Mockito.when(packageRequest.getStatus()).thenReturn("accepted");
        assertThrows(ResponseStatusException.class,()->packageRequestService.unsendRequest(1));
    }

    @Test
    public void unSendRequestTestSuccess(){
        Mockito.when(packageRequestRepo.getPackageRequestById(1)).thenReturn(packageRequest);
        Mockito.when(packageRequest.getStatus()).thenReturn("pending");
        assertEquals("Request deleted",packageRequestService.unsendRequest(1));
    }

    @Test
    public void getRequestTestUserNotFound(){
        Mockito.when(userService.getLoggedInUser()).thenReturn(null);
        assertEquals(null,packageRequestService.getRequest());
    }

    @Test
    public void getRequestTestUserNotFoundException(){
        Mockito.when(userService.getLoggedInUser()).thenThrow(UsernameNotFoundException.class);
        assertEquals(null,packageRequestService.getRequest());
    }

    @Test
    public void getRequestTestSuccess(){
        List<PackageRequest> packageRequests = new ArrayList<>();
        Mockito.when(userService.getLoggedInUser()).thenReturn(user);
        Mockito.when(packageRequestRepo.getAllByDeliverer(user)).thenReturn(packageRequests);
        assertEquals(packageRequests,packageRequestService.getRequest());
    }
}
