package com.example.UserService.member.controller;

import com.example.UserService.member.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/internal/users")
public class InternalUserController {
    private final UserRepository userRepository;

    public InternalUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    //주어진 사용자 id가 있는지 확인
    @GetMapping
    public ResponseEntity<Boolean> checkUserExists(@RequestParam(name="userId") Long followerId){
        return ResponseEntity.ok().body(userRepository.existsById(followerId));
    }
//    @GetMapping
//    public ResponseEntity<UserEntity> getUserById(@PathVariable Long userId) {
//        UserEntity user = internalUserService.findUserById(userId);
//        return ResponseEntity.ok(user);
//    }
}
