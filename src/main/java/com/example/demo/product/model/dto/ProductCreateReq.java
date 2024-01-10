package com.example.demo.product.model.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductCreateReq {
    private String name;
    private Integer categoryIdx;
    private Integer price;
    private Integer salePrice;
    private String deliveryType;
    private String isTodayDeal;
    private String contents;
}
