package com.shipogle.app.controller;

import com.shipogle.app.model.PackageOrder;
import com.shipogle.app.service.PackageOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class PackageOrderController {
    @Autowired
    PackageOrderService packageOrderService;

    /**
     * Get all package order for user
     *
     * @author Nandkumar Kadivar
     * @return List package orders.
     */
    @GetMapping("/package/order/getall")
    public List<PackageOrder> getSenderOrders(){
        return packageOrderService.getSenderOrders();
    }

    /**
     * Get all package order for user
     *
     * @author Nandkumar Kadivar
     * @return List packages.
     */
    @GetMapping("package/order/getAllDelivererRouteOrders")
    public List<PackageOrder> getDelivererRouteOrders(@RequestParam int driver_route_id){
        return packageOrderService.getDelivererRouteOrders(Long.valueOf(driver_route_id));
    }

    /**
     * Cancel order
     *
     * @author Nandkumar Kadivar
     * @param req request
     * @return string response message.
     */
    @PutMapping("/package/order/cancel")
    public String cancelOrder(@RequestBody Map<String,String> req){
        return packageOrderService.cancelOrder(Integer.valueOf(req.get("package_order_id")));
    }

    /**
     * Start order
     *
     * @author Nandkumar Kadivar
     * @param req request
     * @return string response message.
     */
    @PutMapping("package/order/start")
    public String startOrder(@RequestBody Map<String,String> req){
        Integer pickup_code = Integer.valueOf(req.get("pickup_code"));
        Integer order_id = Integer.valueOf(req.get("order_id"));
        return packageOrderService.startPackageOrder(pickup_code,order_id);
    }

    /**
     * End order
     *
     * @author Nandkumar Kadivar
     * @param req request
     * @return string response message.
     */
    @PutMapping("package/order/end")
    public String endOrder(@RequestBody Map<String,String> req){
        return packageOrderService.endPackageOrder(Integer.valueOf(req.get("drop_code")),Integer.valueOf(req.get("order_id")));
    }

    /**
     * Record payament of order
     *
     * @author Nandkumar Kadivar
     * @param req request
     * @return string response message.
     */
    @PutMapping("package/order/recordPayment")
    public String recordPayment(@RequestBody Map<String,String> req){
        return packageOrderService.recordPayment(Integer.valueOf(req.get("package_order_id")));
    }

}
