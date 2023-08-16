package com.shipogle.app.service;

import java.util.*;

import com.shipogle.app.model.*;
import com.shipogle.app.repository.DriverRouteRepository;
import com.shipogle.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DriverRouteFilter {
    @Autowired
    private final DriverRouteRepository driverRouteRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * Constructor
     *
     * @author Shivam Lakhanpal
     * @param driverRouteRepository driver route repository
     */
    public DriverRouteFilter(DriverRouteRepository driverRouteRepository) {
        this.driverRouteRepository = driverRouteRepository;
    }

    /**
     * get filtered driver routes
     *
     * @author Shivam Lakhanpal
     * @param driverRoutes list of driver routes
     * @return list of driver routes
     */
    public List<DriverRoute> filter(List<DriverRoute> driverRoutes) {
        return driverRoutes;
    }

    /**
     * filter driver routes by filters
     *
     * @author Shivam Lakhanpal
     * @param filter filter object
     * @return list of driver routes
     */
    public List<DriverRoute> getDriverRoutesByFilters(DashboardFilter filter) {
        if (filter == null)
            return null;

        return driverRouteRepository.getDriverRoutesByFilters(filter.getSourceCity(), filter.getDestination(), filter.getPickupDataTime(), filter.getMaxPackages()
                , filter.getRadius(), filter.getPrice(), filter.getAllowedCategory(), filter.getCategory());
    }

    /**
     * get driver routes by id
     *
     * @author Shivam Lakhanpal
     * @param driverId driver id
     * @return list of driver routes
     */
    public List<DriverRoute> getDriverRouteById(String driverId) {
        return driverRouteRepository.getDriverRoutes(driverId);
    }

    /**
     * get driver routes by id
     *
     * @author Shivam Lakhanpal
     * @param driverRoute driver route object
     * @return list of driver routes
     */
    public DriverRoute save(DriverRoute driverRoute) {
        return driverRouteRepository.save(driverRoute);
    }
}
