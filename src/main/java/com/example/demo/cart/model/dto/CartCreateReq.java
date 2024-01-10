package com.example.demo.cart.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartCreateReq {
    Integer amount;
    Long productIdx;
}
