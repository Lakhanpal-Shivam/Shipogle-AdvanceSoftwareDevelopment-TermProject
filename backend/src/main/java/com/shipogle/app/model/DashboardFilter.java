package com.shipogle.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Dashboard filter model.
 *
 * @author Shivam Lakhanpal
 */
public class DashboardFilter {
    private String sourceCity;
    private String destination;
    private String pickupDataTime;
    private String maxPackages;
    private String allowedCategory;
    private String radius;
    private String price;
    private String category;
    public String getSourceCity() { return sourceCity; }

    public void setSourceCity(String sourceCity) {
        this.sourceCity = sourceCity;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPickupDataTime() {
        return pickupDataTime;
    }

    public void setPickupDataTime(String pickupDataTime) {
        this.pickupDataTime = pickupDataTime;
    }

    public String getMaxPackages() {
        return maxPackages;
    }

    public void setMaxPackages(String maxPackages) {
        this.maxPackages = maxPackages;
    }

    public String getAllowedCategory() {
        return allowedCategory;
    }

    public void setAllowedCategory(String allowedCategory) {
        this.allowedCategory = allowedCategory;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<DriverRoute> getDriverRoutesByFilters(DashboardFilter filters) {
        List<DriverRoute> routes = new ArrayList<>();

        // Perform filtering logic and add matching routes to the list

        return routes;
    }
}
