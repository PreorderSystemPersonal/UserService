package com.example.UserService.member.Service;

import com.example.UserService.member.Repository.UserRepository;
import com.example.UserService.member.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InternalUserService {
    private final UserRepository userRepository;


    public InternalUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity findUserById(Long userId) {
        return (UserEntity) userRepository.findById(userId);
    }


}
