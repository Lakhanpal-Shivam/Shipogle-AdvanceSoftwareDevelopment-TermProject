package com.shipogle.app.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Package model.
 *
 * @author Nandkumar Kadivar
 */
@Entity
@Table(name="package")
public class Package {
    @Id
    @GeneratedValue
    @Column(name="package_id")
    private Integer id;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "sender_id",referencedColumnName = "user_id")
    private User sender;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="width")
    private float width;

    @Column(name="length")
    private float length;

    @Column(name="height")
    private float heigth;

    @Column(name="pickup_address")
    private String pickup_address;

    @Column(name="drop_address")
    private String drop_address;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updated_at;

    public Integer getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPackageDimension(float length, float width, float heigth){
        this.length = length;
        this.width = width;
        this.heigth = heigth;
    }

    public float getWidth() {
        return width;
    }

    public float getLength() {
        return length;
    }

    public float getHeigth() {
        return heigth;
    }

    public String getPickup_address() {
        return pickup_address;
    }

    public void setPickup_address(String pickup_address) {
        this.pickup_address = pickup_address;
    }

    public String getDrop_address() {
        return drop_address;
    }

    public void setDrop_address(String drop_address) {
        this.drop_address = drop_address;
    }

}
