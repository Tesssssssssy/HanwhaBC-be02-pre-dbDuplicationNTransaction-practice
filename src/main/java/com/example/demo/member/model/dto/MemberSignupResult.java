package com.example.demo.member.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Bean;


@Builder
@Data
public class MemberSignupResult {
    Integer idx;
    Boolean status;
}
