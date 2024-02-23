package com.example.UserService.member.Service;

import com.example.UserService.jwt.JWTUtil;
import com.example.UserService.member.Repository.UserRepository;
import com.example.UserService.member.dto.EmailRequestDTO;
import com.example.UserService.member.dto.JoinDTO;
import com.example.UserService.member.entity.UserEntity;
import com.example.UserService.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

//회원가입 서비스
@Service
public class JoinService {
    @Autowired
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //JWTUtil 주입
    private final RedisUtil redisUtil;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JWTUtil jwtUtil, RedisUtil redisUtil) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.redisUtil = redisUtil;

    }

    public void joinProcess(JoinDTO joinDTO, EmailRequestDTO emailRequestDTO, MultipartFile profileImage) throws IOException {

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        String email = emailRequestDTO.getEmail();
        String introduce = joinDTO.getIntroduce();

        //프로필 이미지를 서버에 업로드하고 경로를 설정
        String profileImagePath = saveProfileImage(profileImage);


        Boolean isExist = userRepository.existsByUsername(username);

        if (isExist) { //만약 중복되는 이름이 있으면 return
            return;
        }

        UserEntity data = new UserEntity();

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setEmail(email);
        data.setIntroduce(introduce);
        data.setProfileImagePath(profileImagePath); // 프로필 이미지 경로 설정
        data.setRole("ROLE_ADMIN");

        System.out.println(data);
        userRepository.save(data);
    }

    private String saveProfileImage(MultipartFile profileImage) throws IOException {
        String profileImagePath = "";// 기본값으로 빈 문자열 설정
        if (profileImage != null && !profileImage.isEmpty()) {
            //프로필 이미지를 저장할 디렉토리 경로 설정(절대 경로 설정)
            String uploadDirectory = "D://바탕화면//spring_proejct(2024.01)//image";

            //업로드할 파일명 생성
            String fileName = profileImage.getOriginalFilename();
            //프로필 이미지를 서버에 업로드하고 저장
            File uploadPath = new File(uploadDirectory);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }
            File dest = new File(uploadPath + "/" + fileName);
            profileImage.transferTo(dest);
            profileImagePath = dest.getAbsolutePath();


        }
        return profileImagePath;
    }
    //회원정보 업데이트-유저이름 인사말
    public UserEntity updateMember(Long id, String username, String introduce){
        UserEntity userEntity = userRepository.findById(id);

        userEntity.setUsername(username);
        userEntity.setIntroduce(introduce);

        System.out.println(username);
        System.out.println(introduce);
        return userRepository.save(userEntity);
    }

    //회원정보 업데이트- 비밀번호 수정
    public UserEntity updatepassword(Long id,String password){
        UserEntity userEntity = userRepository.findById(id);
        userEntity.setPassword(bCryptPasswordEncoder.encode(password));

       return userRepository.save(userEntity);
    }

    //회원정보 업데이트 - 프로필 이미지 수정
    public UserEntity updateProfileImage(Long id,MultipartFile newProfileImage) throws IOException{
        UserEntity userEntity = userRepository.findById(id);
        String profileImagePath = "";// 기본값으로 빈 문자열 설정
        if (newProfileImage != null && !newProfileImage.isEmpty()) {
            //프로필 이미지를 저장할 디렉토리 경로 설정(절대 경로 설정)
            String uploadDirectory = "D://바탕화면//reservation99";

            //업로드할 파일명 생성
            String fileName = newProfileImage.getOriginalFilename();
            //프로필 이미지를 서버에 업로드하고 저장
            File uploadPath = new File(uploadDirectory);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }
            File dest = new File(uploadPath + "/" + fileName);
            newProfileImage.transferTo(dest);
            profileImagePath = dest.getAbsolutePath();


        }
        userEntity.setProfileImagePath(profileImagePath);
        return userRepository.save(userEntity);
    }

    //logout 구현
    @Transactional
    public void logout() {
        // SecurityContextHolder에서 현재 사용자의 인증 정보를 가져옴
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // UserDetails를 구현한 객체인지 확인
        if (principal instanceof UserDetails) {
            // UserDetails 객체로 형변환하여 사용자 이름(username)을 가져옴
            String username = ((UserDetails) principal).getUsername();

            // redisUtil을 사용하여 토큰 가져오고 삭제하는 로직 추가
            String token = redisUtil.getData(username);
            if (Objects.nonNull(token)) {
                redisUtil.deleteData(username);
            } else {
                System.out.println("Token not found for user: " + username);
            }
        } else {
            System.out.println("Principal is not an instance of UserDetails");
        }

    }
}
