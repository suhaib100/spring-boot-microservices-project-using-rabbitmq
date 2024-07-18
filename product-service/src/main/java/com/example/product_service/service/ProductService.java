package com.example.product_service.service;

import com.example.product_service.client.UserServiceClient;
import com.example.product_service.modell.Product;
import com.example.product_service.modell.User;
import com.example.product_service.modell.WishlistRequest;
import com.example.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {


    @Autowired
    private UserServiceClient userServiceClient; // Inject the Feign client

    @Autowired
    private ProductRepository productRepository;


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(String id, Product productDetails) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            return productRepository.save(product);
        }
        return null;
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }



    @Autowired
    private UserServiceClient userClient; // Feign client for User Service


    public void addProductToWishlist(String userId, Product product) {
        User user = userServiceClient.getUserById(userId);
        user.getWishlist().add(product);
        userServiceClient.updateUser(user);
    }

}
