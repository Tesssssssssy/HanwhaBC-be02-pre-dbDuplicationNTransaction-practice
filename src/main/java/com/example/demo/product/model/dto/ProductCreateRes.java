package com.example.demo.product.model.dto;

import com.example.demo.member.model.dto.MemberSignupResult;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductCreateRes {
    Boolean isSuccess;
    Integer code;
    String message;
    ProductCreateResult result;
    Boolean success;
}
