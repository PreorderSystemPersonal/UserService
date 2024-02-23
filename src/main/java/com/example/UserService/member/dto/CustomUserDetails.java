package com.example.UserService.member.dto;

import com.example.UserService.member.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity;


    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
        System.out.println("🎉🎉🎉 userEntitiy까지 옴"+userEntity);
        
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                System.out.println("💡💡💡 권한 얻음!!"+userEntity.getRole());
                return userEntity.getRole();
            }
        });
        System.out.println("❌❌❌"+collection);
        return collection;
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    //이메일 userEntity에서 가져옴
    public String getEmail() {
        return userEntity.getEmail();
    }
    
    

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
