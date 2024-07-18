package com.example.user_service.service;

import com.example.user_service.client.ProductClient;
import com.example.user_service.entity.Product;
import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductClient productClient;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            List<Product> updatedWishlist = user.getWishlist().stream().map(product -> {
                Product fetchedProduct = productClient.getProductById(product.getId());
                if (fetchedProduct != null) {
                    product.setName(fetchedProduct.getName());
                    product.setDescription(fetchedProduct.getDescription());
                }
                return product;
            }).collect(Collectors.toList());

            user.setWishlist(updatedWishlist);
        }

        return user;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(String id, User userDetails) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setName(userDetails.getName());
            user.setWishlist(userDetails.getWishlist());
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public void addProductToWishlist(String userId, Product product) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.getWishlist().add(product);
            userRepository.save(user);

            // Send message to RabbitMQ or handle any other operations
            String message = String.format("{\"userId\":\"%s\", \"productId\":\"%s\"}", userId, product.getId());
            rabbitTemplate.convertAndSend("userQueue", message);
        }
    }
}
