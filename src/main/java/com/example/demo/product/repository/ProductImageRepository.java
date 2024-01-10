package com.example.demo.product.repository;

import com.example.demo.product.model.Product;
import com.example.demo.product.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
