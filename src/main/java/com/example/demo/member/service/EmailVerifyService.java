package com.example.demo.member.service;

import com.example.demo.member.model.EmailVerify;
import com.example.demo.member.repository.EmailVerifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerifyService {
    private final EmailVerifyRepository emailVerifyRepository;

    public Boolean verify(String email, String uuid) {
        Optional<EmailVerify> result = emailVerifyRepository.findByEmail(email);
        if(result.isPresent()){
            EmailVerify emailVerify = result.get();
            if(emailVerify.getUuid().equals(uuid)) {
                return true;
            }
        }

        return false;

    }

    public void create(String email, String uuid) {
        EmailVerify emailVerify = EmailVerify.builder()
                .email(email)
                .uuid(uuid)
                .build();
        emailVerifyRepository.save(emailVerify);
    }
}
