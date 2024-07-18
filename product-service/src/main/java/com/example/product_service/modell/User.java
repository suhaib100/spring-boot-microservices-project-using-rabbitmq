package com.example.product_service.modell;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private String id;
    private String name;
    private List<Product> wishlist = new ArrayList<>(); // Store Product objects directly
}
