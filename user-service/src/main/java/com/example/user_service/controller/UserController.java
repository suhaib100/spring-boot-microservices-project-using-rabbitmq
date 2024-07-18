package com.example.user_service.controller;

import com.example.user_service.client.ProductClient;
import com.example.user_service.entity.Product;
import com.example.user_service.entity.User;
import com.example.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductClient productClient;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }



    @PostMapping("/{userId}/wishlist")
    public void addProductToWishlist(@PathVariable String userId, @RequestBody Product productRequest) {
        // Fetch product details from ProductService using Feign Client

        if (productRequest != null) {
            // Call userService method to add the product to the wishlist
            userService.addProductToWishlist(userId, productRequest);
        } else {
            // Handle case where product with given ID does not exist
            // Optionally return a 404 or handle the error as needed
        }
    }

    @PostMapping("/notify")
    public void notifyUser(@RequestBody String message) {
        System.out.println("Notification received: " + message);
        // Here you can add code to handle the notification, e.g., send an email or log the message
    }
}
