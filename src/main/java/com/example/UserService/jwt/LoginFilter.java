package com.example.UserService.jwt;

import com.example.UserService.member.dto.CustomUserDetails;
import com.example.UserService.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    //JWTUtil 주입
    private final JWTUtil jwtUtil;

    @Autowired
    private final RedisUtil redisUtil; //  객체의 인스턴스 생성
    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RedisUtil redisUtil) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
        setFilterProcessesUrl("/UserService/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 username, password 추출
        //이메일 인증으로 해도 무조건 username으로 받아야 함
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println("📌📌받아옴 "+username);
        System.out.println("📌📌받아옴 "+password);

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);


        System.out.println(authToken);
        //token에 담은 검증을 위한 AuthenticationManager로 전달
        System.out.println(authenticationManager.authenticate(authToken));
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        //UserDetailsS
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        System.out.println("✅✅ customUserDetails 받아옴");

        String username = customUserDetails.getUsername();
        System.out.println("✅✅ username 받아옴 "+username);

        String email = customUserDetails.getEmail();
        System.out.println("✅✅ email 받아옴 "+email);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        System.out.println("✅✅ authorities 받아옴 ");
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        System.out.println("✅✅ iterator 받아옴 ");
        GrantedAuthority auth = iterator.next();
        System.out.println("✅✅ auth 받아옴 ");


        String role = auth.getAuthority();

        System.out.println("✅✅ role 받아옴 "+role);

        String token = jwtUtil.createJwt(email, role, 30*60*1000L);

        System.out.println("✅✅ token 받아옴 "+token);
        //로그아웃 구분하기 위해 redis에 저장
        redisUtil.setData(email,token);
        response.addHeader("Authorization", "Bearer " + token);
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        //로그인 실패시 401 응답 코드 반환
        response.setStatus(401);
        System.out.println("Authentication failed: " + failed.getMessage()); // 실패 이유 출력
        System.out.println("fail");
    }


}
