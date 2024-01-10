package com.example.demo.member.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberSignupReq {
    String email;
    String nickname;
    String password;
}
