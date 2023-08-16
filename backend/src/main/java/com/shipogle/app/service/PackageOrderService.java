package com.shipogle.app.service;

import com.shipogle.app.model.PackageOrder;
import com.shipogle.app.model.PackageRequest;

import java.util.List;

public interface PackageOrderService {
    public String createPackageOrder(PackageRequest packageRequest);

    public boolean isPackageOrderExist(Integer package_id);

    public String cancelOrder(Integer order_id);

    public List<PackageOrder> getSenderOrders();

    public List<PackageOrder> getDelivererRouteOrders(Long driver_route_id);

    public String recordPayment(Integer package_order_id);

    public String startPackageOrder(int pickup_code,int order_id);

    public String endPackageOrder(int drop_code,int order_id);
}
