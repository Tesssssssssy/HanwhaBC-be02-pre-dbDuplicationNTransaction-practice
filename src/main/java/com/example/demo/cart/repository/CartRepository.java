package com.example.demo.cart.repository;

import com.example.demo.cart.model.Cart;
import com.example.demo.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    public List<Cart> findAllByMember(Member member);
    public void deleteByIdAndMember(Long id, Member member);
}
