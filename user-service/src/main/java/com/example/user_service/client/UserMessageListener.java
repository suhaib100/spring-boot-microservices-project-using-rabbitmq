package com.example.user_service.client;

import com.example.user_service.entity.Product;
import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UserMessageListener {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductClient productClient; // Inject ProductClient

    @RabbitListener(queues = "userQueue")
    public void receiveMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);
            String userId = jsonNode.get("userId").asText();
            String productId = jsonNode.get("productId").asText();

            // Fetch product details using Feign client
            Product product = productClient.getProductById(productId);

            if (product != null) {
                // Retrieve user and add Product to the wishlist
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    user.getWishlist().add(product);
                    userRepository.save(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
