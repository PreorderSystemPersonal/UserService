package com.example.UserService.member.Service;

import com.example.UserService.member.Repository.UserRepository;
import com.example.UserService.member.dto.CustomUserDetails;
import com.example.UserService.member.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //토큰 인증정보를 조회하기 위해 사용(이메일)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //db에서 이메일 조회
        UserEntity userData = userRepository.findByEmail(email);
        System.out.println("✨✨✨✨"+userData);
        if(userData!=null){
            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            System.out.println("✔✔✔ userData가 customuserdetails로 넘어감");
            return new CustomUserDetails(userData);
        }
        return null;
    }

    public UserEntity loadUserById(Long id) {
        return userRepository.findById(id);

    }
}
