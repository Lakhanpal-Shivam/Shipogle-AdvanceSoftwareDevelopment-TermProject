package com.shipogle.app.repository;

import com.shipogle.app.model.PackageRequest;
import com.shipogle.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PackageRequestRepository extends JpaRepository<PackageRequest,Integer> {

    /**
     * getAllByDeliverer is a method to get all package requests by deliverer
     *
     * @author Nandkumar Kadivar
     * @param package_request_id package request id
     * @return List<PackageRequest>
     */
    PackageRequest getPackageRequestById(Integer package_request_id);

    /**
     * getAllByDeliverer is a method to get all package requests by deliverer
     *
     * @author Nandkumar Kadivar
     * @param package_id package id
     * @return List<PackageRequest>
     */
    List<PackageRequest> getAllBy_package_Id(Integer package_id);

    /**
     * getAllByDeliverer is a method to get all package requests by deliverer
     *
     * @author Nandkumar Kadivar
     * @param package_id package id
     * @param deliverer_id deliverer id
     * @return List<PackageRequest>
     */
    int countAllBy_package_IdAndDeliverer_Id(@Param("_package_Id") Integer package_id,@Param("Deliverer_Id") Integer deliverer_id);

    /**
     * getAllByDeliverer is a method to get all package requests by deliverer
     *
     * @author Nandkumar Kadivar
     * @param package_id package id
     * @param driver_route_id driver route id
     * @return List<PackageRequest>
     */
    int countAllBy_package_IdAndDriverRoute_Id(@Param("_package_Id") Integer package_id,@Param("DriverRoute_Id") Long driver_route_id);

    /**
     * getAllByDeliverer is a method to get all package requests by deliverer
     *
     * @author Nandkumar Kadivar
     * @param deliverer deliverer object
     * @return List<PackageRequest>
     */
    List<PackageRequest> getAllByDeliverer(User deliverer);
}
