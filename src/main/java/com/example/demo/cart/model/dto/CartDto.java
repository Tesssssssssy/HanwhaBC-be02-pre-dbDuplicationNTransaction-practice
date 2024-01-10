package com.example.demo.cart.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDto {
    Long idx;
    Long productIdx;
    Integer brandIdx;
    Integer amount;
    String name;
    Integer price;
    Integer salePrice;
    String deliveryType;
    String isTodayDeal;
    String filename;
}
