package com.shoestock.desktop.model;

import lombok.Data;

@Data
public class Product {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private int quantity;
    private String type;
}