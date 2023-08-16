package com.shipogle.app.controller;

import com.shipogle.app.service.PackageOrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class PackageOrderControllerTests {
    @InjectMocks
    PackageOrderController packageOrderController;
    @Mock
    PackageOrderServiceImpl packageOrderService;

    private final int TEST_PICKUP_CODE = 1234;
    private final int TEST_DROP_CODE = 1254;
    @Test
    public void createPackageTest() {

        packageOrderController.getSenderOrders();

        verify(packageOrderService,times(1)).getSenderOrders();
    }

    @Test
    public void delivererOrderTest() {

        packageOrderController.getDelivererRouteOrders(1);

        verify(packageOrderService,times(1)).getDelivererRouteOrders(1L);
    }

    @Test
    public void cancelOrderTest() {
        Map<String, String> req = new HashMap<>();
        req.put("package_order_id","1");
        packageOrderController.cancelOrder(req);

        verify(packageOrderService,times(1)).cancelOrder(1);
    }

    @Test
    public void startOrderTest() {
        Map<String, String> req = new HashMap<>();
        req.put("pickup_code","1234");
        req.put("order_id","1");
        packageOrderController.startOrder(req);

        verify(packageOrderService,times(1)).startPackageOrder(TEST_PICKUP_CODE,1);
    }

    @Test
    public void endOrderTest() {
        Map<String, String> req = new HashMap<>();
        req.put("drop_code","1254");
        req.put("order_id","1");
        packageOrderController.endOrder(req);

        verify(packageOrderService,times(1)).endPackageOrder(TEST_DROP_CODE,1);
    }

    @Test
    public void paymentRecordTest() {
        Map<String, String> req = new HashMap<>();
        req.put("package_order_id","1");
        packageOrderController.recordPayment(req);

        verify(packageOrderService,times(1)).recordPayment(1);
    }
}
