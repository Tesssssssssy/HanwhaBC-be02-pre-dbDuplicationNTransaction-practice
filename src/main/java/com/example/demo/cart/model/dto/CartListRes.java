package com.example.demo.cart.model.dto;

import com.example.demo.member.model.dto.MemberSignupResult;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CartListRes {
    Boolean isSuccess;
    Integer code;
    String message;
    List<CartDto> result;
    Boolean success;
}
