package com.demo.carsharing.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String model;
    @Enumerated(EnumType.STRING)
    private Type type;
    private Integer inventory;
    private BigDecimal dailyFee;
    private boolean deleted;
    private String bucketName;
    private String keyName;
    private String presignedUrl;

    public enum Type {
        SEDAN,
        SUV,
        HATCHBACK,
        UNIVERSAL,
    }
}
