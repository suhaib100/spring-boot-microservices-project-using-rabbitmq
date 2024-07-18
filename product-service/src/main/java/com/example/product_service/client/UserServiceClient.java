package com.example.product_service.client;

import com.example.product_service.modell.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service",url = "http://localhost:8081")
public interface UserServiceClient {


    @GetMapping("/users/{id}")
    User getUserById(@PathVariable("id") String id);

    @PutMapping("/users/{id}")
    User updateUser(@PathVariable("id") String id, @RequestBody User user);

}
