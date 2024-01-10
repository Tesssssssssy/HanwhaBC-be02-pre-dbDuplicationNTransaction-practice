package com.example.demo.member.controller;

import com.example.demo.member.model.dto.*;
import com.example.demo.member.service.EmailVerifyService;
import com.example.demo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final EmailVerifyService emailVerifyService;



    @RequestMapping(method = RequestMethod.POST, value = "/authenticate")
    public ResponseEntity login(@RequestBody MemberLoginReq memberLoginReq){
        MemberLoginRes response = MemberLoginRes.builder()
                .token(memberService.login(memberLoginReq))
                .build();
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public ResponseEntity signup (@RequestBody MemberSignupReq memberSignupReq) {

        MemberSignupResult result = memberService.signup(memberSignupReq);

        memberService.sendEmail(result.getIdx(), memberSignupReq);
        MemberSignupRes response = MemberSignupRes.builder()
                .code(1000)
                .message("요청 성공.")
                .success(true)
                .isSuccess(true)
                .result(result)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/confirm")
    public RedirectView verify(String email, String token, String jwt) {
        if(emailVerifyService.verify(email, token)) {

            memberService.update(email);

            return new RedirectView("http://localhost:3000/emailconfirm/" + jwt);
        }
        return new RedirectView("http://localhost:3000/emailCertError");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/checkemail")
    public ResponseEntity checkemail(String email) {

        if(memberService.getMemberByEmail(email)) {
            return ResponseEntity.ok().body(true);
        }
        return ResponseEntity.ok().body(false);
    }

}
