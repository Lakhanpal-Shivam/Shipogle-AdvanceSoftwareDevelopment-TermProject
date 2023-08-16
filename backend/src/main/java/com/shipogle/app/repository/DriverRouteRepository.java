package com.shipogle.app.repository;

import com.shipogle.app.model.DriverRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRouteRepository extends JpaRepository<DriverRoute, Long> {

    /**
     * getDriverRoutesByFilters is a method to get driver routes by filters
     *
     * @author Shivam Lakhanpal
     * @param sourceCity source city
     * @param destination destination city
     * @param pickupDataTime pickup date time
     * @param maxPackages max packages
     * @param radius radius
     * @param price price
     * @param allowedCategory allowed category
     * @param category category
     * @return List<DriverRoute>
     */
    @Query ("select d from DriverRoute d where (?1 is null or d.sourceCity = ?1)  and (?2 is null OR d.destinationCity = ?2) and (?3 is null OR d.pickupDate = ?3)and (?4 is null OR d.maxPackages >= ?4)and (?5 is null OR d.radius >= ?5)and (?6 is null OR d.price <= ?6)")
    List<DriverRoute> getDriverRoutesByFilters(String sourceCity, String destination, String pickupDataTime, String maxPackages, String radius, String price, String allowedCategory, String category);

    /**
     * getDriverRouteById is a method to get driver route by id
     *
     * @author Shivam Lakhanpal
     * @param driverRouteId driver route id
     * @return DriverRoute object
     */
    DriverRoute getDriverRouteById(Long driverRouteId);

    /**
     * getDriverRoutes is a method to get driver routes by driver id
     *
     * @author Shivam Lakhanpal
     * @param driverId driver id
     * @return List<DriverRoute>
     */
    @Query ("select d from DriverRoute d where (d.driverId = ?1)")
    List<DriverRoute> getDriverRoutes(String driverId);
}