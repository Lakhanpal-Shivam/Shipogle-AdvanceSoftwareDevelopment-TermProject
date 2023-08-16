package com.shipogle.app.repository;

import com.shipogle.app.model.PackageOrder;
import com.shipogle.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageOrderRepository extends JpaRepository<PackageOrder,Integer> {

    /**
     * getPackageOrderById is a method to get package order by id
     *
     * @author Nandkumar Kadivar
     * @param package_order_id package order id
     * @return PackageOrder object
     */
    PackageOrder getPackageOrderById(Integer package_order_id);

    /**
     * getBy_package_Id is a method to get package order by package id
     *
     * @author Nandkumar Kadivar
     * @param package_id package id
     * @return PackageOrder object
     */
    PackageOrder getBy_package_Id(Integer package_id);

    /**
     * getAllBySender is a method to get all package orders by sender
     *
     * @author Nandkumar Kadivar
     * @param user user object
     * @return List<PackageOrder>
     */
    List<PackageOrder> getAllBySender(User user);

    /**
     * getAllByReceiver is a method to get all package orders by receiver
     *
     * @author Nandkumar Kadivar
     * @param deliver_route_id deliver route id
     * @return List<PackageOrder>
     */
    List<PackageOrder> getAllByDriverRoute_Id(Long deliver_route_id);
}