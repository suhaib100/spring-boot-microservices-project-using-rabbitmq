package com.example.product_service.modell;

import lombok.Data;

@Data
public class WishlistRequest {
    private String userId; // ID of the user
    private Product product; // Store the entire product ob
}
