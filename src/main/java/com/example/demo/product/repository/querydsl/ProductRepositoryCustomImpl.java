package com.example.demo.product.repository.querydsl;

import com.example.demo.member.model.QMember;
import com.example.demo.product.model.Product;
import com.example.demo.product.model.QProduct;
import com.example.demo.product.model.QProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.jpa.JPAExpressions.select;

public class ProductRepositoryCustomImpl extends QuerydslRepositorySupport implements ProductRepositoryCustom {
    public ProductRepositoryCustomImpl() {
        super(Product.class);
    }

    @Override
    public Page<Product> findList(Pageable pageable) {
        QProduct product = new QProduct("product");


        List<Product> result = from(product)
                .leftJoin(product.productImages).fetchJoin()
                .leftJoin(product.brandIdx).fetchJoin()
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch().stream().collect(Collectors.toList());

        return new PageImpl<>(result, pageable, pageable.getPageSize());
    }
}
