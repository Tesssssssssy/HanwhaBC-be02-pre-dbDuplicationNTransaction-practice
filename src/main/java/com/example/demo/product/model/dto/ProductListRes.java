package com.example.demo.product.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ProductListRes {
    Boolean isSuccess;
    Integer code;
    String message;
    List<ProductReadRes> result;
    Boolean success;
}
