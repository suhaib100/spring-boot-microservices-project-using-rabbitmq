package com.example.product_service.config;

import com.example.product_service.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductMessageListener {
    @Autowired
    private ProductService productService;

    @Autowired
    private RestTemplate restTemplate;

    @RabbitListener(queues = "productQueue")
    public void receiveMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);
            String userId = jsonNode.get("userId").asText();
            String productId = jsonNode.get("productId").asText();

            // Example processing: Notify user about product update
            String userNotificationUrl = "http://localhost:8081/users/notify"; // Replace with actual endpoint
            String notificationMessage = "Product " + productId + " has been updated.";
            restTemplate.postForEntity(userNotificationUrl, notificationMessage, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
