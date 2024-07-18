package com.example.user_service.client;


import com.example.user_service.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service",url = "http://localhost:8082")
public interface ProductClient {

    @GetMapping("/products/{id}")
    Product getProductById(@PathVariable String id);

}
