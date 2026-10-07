package com.example.service;

import com.example.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductService {
    private final List<Product> products = new ArrayList<>();

    public ProductService() {
        products.add(new Product(1, "Laptop ASUS", 18000000, "Điện tử"));
        products.add(new Product(2, "Điện thoại Samsung", 9500000, "Điện thoại"));
        products.add(new Product(3, "Tai nghe Sony", 2200000, "Phụ kiện"));
    }

    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(products);
    }

    public void addProduct(Product product) {
        if (product == null) {
            return;
        }

        int nextId = products.stream()
                .mapToInt(Product::getId)
                .max()
                .orElse(0) + 1;

        product.setId(nextId);
        products.add(product);
    }
}
