package com.example.user_service.client;

import com.example.user_service.entity.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new ProductReadConverter());
        converters.add(new ProductWriteConverter());
        return new MongoCustomConversions(converters);
    }

    @ReadingConverter
    public static class ProductReadConverter implements Converter<String, Product> {
        @Override
        public Product convert(String source) {
            String[] parts = source.split(",");
            Product product = new Product();
            product.setId(parts[0]);
            if (parts.length > 1) product.setName(parts[1]);
            if (parts.length > 2) product.setDescription(parts[2]);
            return product;
        }
    }

    @WritingConverter
    public static class ProductWriteConverter implements Converter<Product, String> {
        @Override
        public String convert(Product source) {
            return source.getId() + "," + source.getName() + "," + source.getDescription();
        }
    }
}
