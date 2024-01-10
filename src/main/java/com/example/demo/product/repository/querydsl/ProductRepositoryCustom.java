package com.example.demo.product.repository.querydsl;
import com.example.demo.product.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {
    public Page<Product> findList(Pageable pageable);
}
