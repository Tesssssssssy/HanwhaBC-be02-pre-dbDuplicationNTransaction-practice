package com.example.demo.product.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Builder
@Data
public class ProductReadRes2 {
    Boolean isSuccess;
    Integer code;
    String message;
    ProductReadRes result;
    Boolean success;
}