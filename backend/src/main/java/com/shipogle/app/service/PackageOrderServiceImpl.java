package com.shipogle.app.service;

import com.shipogle.app.model.PackageOrder;
import com.shipogle.app.model.PackageRequest;
import com.shipogle.app.model.User;
import com.shipogle.app.repository.PackageOrderRepository;
import com.shipogle.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;

import static com.shipogle.app.utility.Const.RANDOM_LOWER_BOUND;
import static com.shipogle.app.utility.Const.RANDOM_UPPER_BOUND;

@Service
public class PackageOrderServiceImpl implements PackageOrderService {
    @Autowired
    PackageOrderRepository packageOrderRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    UserService userService;

    /**
     * Create package order
     *
     * @author Nandkumar Kadivar
     * @param packageRequest request of package delivery
     * @return response message.
     */
    @Override
    public String createPackageOrder(PackageRequest packageRequest){
        PackageOrder packageOrder = new PackageOrder();
        Random random = new Random();

        packageOrder.set_package(packageRequest.get_package());
        packageOrder.setDeliverer(packageRequest.getDeliverer());
        packageOrder.setSender(packageRequest.getSender());
        packageOrder.setDriverRoute(packageRequest.getDriverRoute());
        String pickup_code = String.valueOf(random.nextInt(RANDOM_UPPER_BOUND - RANDOM_LOWER_BOUND) + RANDOM_LOWER_BOUND);
        String dop_code = String.valueOf(random.nextInt(RANDOM_UPPER_BOUND - RANDOM_LOWER_BOUND) + RANDOM_LOWER_BOUND);
        packageOrder.setPaymentStatus(0); //Set payment status 0 indicates payment is not done
        packageOrder.setPickup_code(Integer.valueOf(pickup_code));
        packageOrder.setDrop_code(Integer.valueOf(dop_code));

        packageOrderRepo.save(packageOrder);

        return "order created";
    }

    /**
     * Check that order os already exist in database
     *
     * @author Nandkumar Kadivar
     * @param package_id package id
     * @return boolean response.
     */
    @Override
    public boolean isPackageOrderExist(Integer package_id){
        PackageOrder order = packageOrderRepo.getBy_package_Id(package_id);
        return order != null && !order.isCanceled();
    }

    /**
     * Cancel order and change payment status to refund
     *
     * @author Nandkumar Kadivar
     * @param order_id package order id
     * @return string response message.
     */
    @Override
    public String cancelOrder(Integer order_id){
        PackageOrder order = packageOrderRepo.getPackageOrderById(order_id);
        if(order == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found");
        if(order.isCanceled())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already canceled");
        if(order.isStarted())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot cancel order");
        order.setCanceled(true);
        order.setPaymentStatus(-1);
        packageOrderRepo.save(order);

        return "order is canceled";
    }

    /**
     * Fetch all the orders for the sender
     *
     * @author Nandkumar Kadivar
     * @return list of package orders.
     */
    @Override
    public List<PackageOrder> getSenderOrders(){
        try {
            User user = userService.getLoggedInUser();

            return packageOrderRepo.getAllBySender(user);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Fetch all the orders for the deliverer route
     *
     * @author Nandkumar Kadivar
     * @return list of package orders.
     */
    @Override
    public List<PackageOrder> getDelivererRouteOrders(Long driver_route_id){
        try {
            return packageOrderRepo.getAllByDriverRoute_Id(driver_route_id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Record payment for the package order
     *
     * @author Nandkumar Kadivar
     * @return string response message.
     */
    @Override
    public String recordPayment(Integer package_order_id){
        try {
            PackageOrder order = packageOrderRepo.getById(package_order_id);
            order.setPaymentStatus(1);
            packageOrderRepo.save(order);
            return "Payment is recorded";
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Start order
     *
     * @author Nandkumar Kadivar
     * @param pickup_code package pickup code
     * @param order_id package order id
     * @return string response.
     */
    @Override
    public String startPackageOrder(int pickup_code,int order_id){
        PackageOrder order = packageOrderRepo.getById(order_id);

        if(!order.isCanceled() && order.getPickup_code() == pickup_code){
            order.setStarted(true);
            packageOrderRepo.save(order);
            return "Order started";
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to start the order");
    }

    /**
     * End order
     *
     * @author Nandkumar Kadivar
     * @param drop_code package drop code
     * @param order_id package order id
     * @return string response.
     */
    @Override
    public String endPackageOrder(int drop_code,int order_id){
        PackageOrder order = packageOrderRepo.getById(order_id);

        if(isAbleToEndOrder(order,drop_code)){
            order.setDelivered(true);
            packageOrderRepo.save(order);
            return "Order ended";
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to end the order");
    }

    /**
     * Check that is order able to end
     *
     * @author Nandkumar Kadivar
     * @param order package pickup code
     * @param drop_code package order id
     * @return string response.
     */
    private boolean isAbleToEndOrder(PackageOrder order, int drop_code){
        boolean active_order = !order.isCanceled() && order.isStarted();
        return active_order && order.getDrop_code() == drop_code;
    }
}
