package com.example.demo.likes.controller;

import com.example.demo.likes.service.LikesService;
import com.example.demo.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikesController {
    private final LikesService likesService;

    @RequestMapping(method = RequestMethod.GET, value = "/{idx}")
    public ResponseEntity likes(@AuthenticationPrincipal Member member, @PathVariable Long idx) {
        try {
            likesService.likes(member, idx);
        } catch (Exception e) {
            System.out.println("동시성 에러 발생");
            likesService.likes(member, idx);
        }

        return ResponseEntity.ok().body("ok");
    }
}
