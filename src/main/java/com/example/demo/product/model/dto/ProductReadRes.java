package com.example.demo.product.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductReadRes {
    Long idx;
    String name;
    Integer brandIdx;
    Integer categoryIdx;
    Integer price;
    Integer salePrice;
    String deliveryType;
    String isTodayDeal;
    String filename;
    Boolean like_check;
}
