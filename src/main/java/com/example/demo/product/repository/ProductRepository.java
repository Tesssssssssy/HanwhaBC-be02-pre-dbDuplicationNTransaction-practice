package com.example.demo.product.repository;

import com.example.demo.product.model.Product;
import com.example.demo.product.repository.querydsl.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    public Optional<Product> findByName(String name);

    // 낙관적 락
    @Lock(LockModeType.OPTIMISTIC)
    public Optional<Product> findById(Long id);

    
    // 비관적 락
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    public Optional<Product> findById(Long id);


    @Query("SELECT p FROM Product p " +
            "JOIN FETCH p.productImages " +
            "JOIN FETCH p.brandIdx")
    public List<Product> findAllQuery();

    @Query(nativeQuery = true, value = "SELECT * FROM product AS p " +
            "LEFT JOIN product_image AS pi ON p.id = pi.product_id " +
            "LEFT JOIN member AS m ON p.member_id = m.id LIMIT :page, :size")
    public List<Product> findAllQueryWithPage(Integer page, Integer size);

}
