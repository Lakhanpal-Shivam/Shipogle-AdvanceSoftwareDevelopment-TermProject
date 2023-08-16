package com.shipogle.app.model;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * Driver route model.
 *
 * @author Shivam Lakhanpal
 */
@Entity
@Table(name = "driver_routes")
public class DriverRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driverId")
    private String driverId;

    @Column(name = "driverName")
    private String driverName;

    @Column(name = "sourceCity")
    private String sourceCity;

    @Column(name = "sourceAddress")
    private String sourceAddress;

    @Column(name = "sourceCityReferenceId")
    private String sourceCityReferenceId;

    @Column(name = "destinationCity")
    private String destinationCity;

    @Column(name = "destinationAddress")
    private String destinationAddress;

    @Column(name = "destinationCityReferenceId")
    private String destinationCityReferenceId;

    @Column(name = "maxPackages")
    private int maxPackages;

    @Column(name = "maxLength")
    private int maxLength;

    @Column(name = "maxWidth")
    private int maxWidth;

    @Column(name = "maxHeight", nullable = false)
    private int maxHeight;

    @Column(name = "pickupDate", nullable = false)
    private Date pickupDate;

    @Column(name = "dropoffDate")
    private Date dropoffDate;

    @Column(name = "daysToDeliver")
    private int daysToDeliver;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "pickupLocationCoords")
    private List<Double> pickupLocationCoords;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "dropoffLocationCoords")
    private List<Double> dropoffLocationCoords;

    @ElementCollection()
    @Column(name = "allowedCategory")
    private List<String> allowedCategory;

    @Column(name = "radius")
    private int radius;

    @Column(name = "price")
    private int price;

    public DriverRoute() {
        // default constructor for JPA
    }

    public long getDriverRouteId() {
        return id;
    }

    public void setDriverRouteId(long id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getSourceCity() {
        return sourceCity;
    }

    public void setSourceCity(String sourceCity) {
        this.sourceCity = sourceCity;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getSourceCityReferenceId() {
        return sourceCityReferenceId;
    }

    public void setSourceCityReferenceId(String sourceCityReferenceId) {
        this.sourceCityReferenceId = sourceCityReferenceId;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getDestinationCityReferenceId() {
        return destinationCityReferenceId;
    }

    public void setDestinationCityReferenceId(String destinationCityReferenceId) {
        this.destinationCityReferenceId = destinationCityReferenceId;
    }

    public int getMaxPackages() {
        return maxPackages;
    }

    public void setMaxPackages(int maxPackages) {
        this.maxPackages = maxPackages;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public Date getDropoffDate() {
        return dropoffDate;
    }

    public void setDropoffDate(Date dropoffDate) {
        this.dropoffDate = dropoffDate;
    }

    public int getDaysToDeliver() {
        return daysToDeliver;
    }

    public void setDaysToDeliver(int daysToDeliver) {
        this.daysToDeliver = daysToDeliver;
    }

    public List<Double> getPickupLocationCoords() {
        return pickupLocationCoords;
    }

    public void setPickupLocationCoords(List<Double> pickupLocationCoords) {
        this.pickupLocationCoords = pickupLocationCoords;
    }

    public List<Double> getDropoffLocationCoords() {
        return dropoffLocationCoords;
    }

    public void setDropoffLocationCoords(List<Double> dropoffLocationCoords) {
        this.dropoffLocationCoords = dropoffLocationCoords;
    }

    public List<String> getAllowedCategory() {
        return allowedCategory;
    }

    public void setAllowedCategory(List<String> allowedCategory) {
        this.allowedCategory = allowedCategory;
    }

    public int getRadius(){
        return radius;
    }

    public void setRadius(int radius){
        this.radius = radius;
    }

    public int getPrice(){
        return price;
    }

    public void setPrice(int price){
        this.price = price;
    }
}