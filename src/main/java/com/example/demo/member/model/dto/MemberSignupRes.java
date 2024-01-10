package com.example.demo.member.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberSignupRes {
    Boolean isSuccess;
    Integer code;
    String message;
    MemberSignupResult result;
    Boolean success;
}
