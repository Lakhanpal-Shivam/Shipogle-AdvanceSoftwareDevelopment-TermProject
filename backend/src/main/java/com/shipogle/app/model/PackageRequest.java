package com.shipogle.app.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * PackageRequest model.
 *
 * @author Nandkumar Kadivar
 */
@Entity
@Table(name="package_request")
public class PackageRequest {
    @Id
    @GeneratedValue
    @Column(name="package_request_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_id",referencedColumnName = "user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "deliverer_id",referencedColumnName = "user_id")
    private User deliverer;

    @ManyToOne
    @JoinColumn(name = "package_id",referencedColumnName = "package_id")
    private Package _package;

    @ManyToOne
    @JoinColumn(name="driver_route_id",referencedColumnName = "id")
    private DriverRoute driverRoute;

    @Column(name="ask_price")
    private Float aksPrice;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime created_at;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updated_at;

    @Column(name = "status")
    private String status;

    public Integer getPackage_request_id() {
        return id;
    }

    public void setPackage_request_id(Integer package_request_id) {
        this.id = package_request_id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(User deliverer) {
        this.deliverer = deliverer;
    }

    public Package get_package() {
        return _package;
    }

    public void set_package(Package _package) {
        this._package = _package;
    }

    public DriverRoute getDriverRoute() {
        return driverRoute;
    }

    public void setDriverRoute(DriverRoute driverRoute) {
        this.driverRoute = driverRoute;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public Float getAksPrice() {
        return aksPrice;
    }

    public void setAksPrice(Float aksPrice) {
        this.aksPrice = aksPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
