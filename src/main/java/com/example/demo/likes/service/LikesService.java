package com.example.demo.likes.service;

import com.example.demo.member.model.Member;
import com.example.demo.product.model.Product;
import com.example.demo.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.StaleStateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final ProductRepository productRepository;

    @Transactional
    public void likes(Member member, Long idx) throws StaleStateException {

        Optional<Product> result = productRepository.findById(idx);

        if (result.isPresent()) {
            Product product = result.get();
            product.increaseLikeCount();
            product = productRepository.save(product);
        }
    }
}
