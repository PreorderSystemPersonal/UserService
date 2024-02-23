package com.example.UserService.member.Repository;

import com.example.UserService.member.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Boolean existsByUsername(String username);

    //username을 받아 DB 테이블에서 회원을 조회하는 메소드 작성
    UserEntity findByUsername(String username);

    //이메일을 받아 db 테이블에서 이메일 조회하는 메소드 작성
    UserEntity findByEmail(String email);

    UserEntity findById(Long id);

    //유저 db에 팔로우 목록이 있는지 확인
    Boolean existsById(Long id);
}