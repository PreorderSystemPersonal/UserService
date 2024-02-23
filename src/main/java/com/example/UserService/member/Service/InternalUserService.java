package com.example.UserService.member.Service;

import com.example.UserService.member.Repository.UserRepository;
import com.example.UserService.member.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

//주어진 사용자 id로 사용자를 찾아 반환 - FollowService와 연결(feign-client)
@Slf4j
@Service
public class InternalUserService {
    private final UserRepository userRepository;


    public InternalUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity findUserById(Long userId) {
        return (UserEntity) userRepository.findById(userId);
//                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }


}
