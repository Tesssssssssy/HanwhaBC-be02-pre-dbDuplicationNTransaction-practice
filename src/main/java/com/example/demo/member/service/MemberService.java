package com.example.demo.member.service;

import com.example.demo.member.model.Member;
import com.example.demo.member.model.dto.MemberLoginReq;
import com.example.demo.member.model.dto.MemberSignupReq;
import com.example.demo.member.model.dto.MemberSignupRes;
import com.example.demo.member.model.dto.MemberSignupResult;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender emailSender;
    private final EmailVerifyService emailVerifyService;


    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private int expiredTimeMs;

    @Value("${message.email.from}")
    private String originEmail;

    public Boolean getMemberByEmail(String email) {
        Optional<Member> result = memberRepository.findByEmail(email);
        if(result.isPresent()) {
            return true;
        }

        return false;
    }

    public String login(MemberLoginReq memberLoginReq) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(memberLoginReq.getUsername(), memberLoginReq.getPassword()));

        if (authentication.isAuthenticated()) {
            Integer id = ((Member)authentication.getPrincipal()).getId();
            return JwtUtils.generateAccessToken(memberLoginReq.getUsername(), id, secretKey, expiredTimeMs);
        }

        return null;
    }

    public void sendEmail(Integer id, MemberSignupReq memberSignupReq) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(originEmail);
        message.setTo(memberSignupReq.getEmail());
        message.setSubject("[심마켓] 이메일 인증");
        String uuid = UUID.randomUUID().toString();
        message.setSentDate(new Date(System.currentTimeMillis()));
        message.setText("http://localhost:8080/member/confirm?email="
                + memberSignupReq.getEmail()
                + "&token=" + uuid
                + "&jwt=" + JwtUtils.generateAccessToken(memberSignupReq.getEmail(), id, secretKey, expiredTimeMs)
        );
        emailSender.send(message);

        emailVerifyService.create(memberSignupReq.getEmail(), uuid);
    }


    public MemberSignupResult signup(MemberSignupReq memberSignupReq) {
        if (!memberRepository.findByEmail(memberSignupReq.getEmail()).isPresent()) {
            Member member = memberRepository.save(Member.builder()
                    .email(memberSignupReq.getEmail())
                    .nickname(memberSignupReq.getNickname())
                    .password(passwordEncoder.encode(memberSignupReq.getPassword()))
                    .authority("ROLE_USER")
                    .status(false)
                    .build());

            return MemberSignupResult.builder()
                    .idx(member.getId())
                    .status(member.getStatus())
                    .build();
        }

        return null;

    }


    public void update(String email) {
        Optional<Member> result = memberRepository.findByEmail(email);
        if(result.isPresent()) {
            Member member = result.get();
            member.setStatus(true);
            memberRepository.save(member);
        }
    }
}
