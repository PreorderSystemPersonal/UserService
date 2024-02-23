package com.example.UserService.member.Service;

import com.example.UserService.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
public class MailSendService {

    @Autowired
    private JavaMailSender mailSender; //이메일 인증

    private int authNumber; //이메일 인증번혼
    private boolean checked; // 이메일 인증 여부 체크

    @Autowired
    private RedisUtil redisUtil;



    public boolean CheckAuthNum(String email,String authNum){
        if(redisUtil.getData(authNum)==null){
            return false;
        }
        else if(redisUtil.getData(authNum).equals(email)){
            checked = true;
            return true;
        }
        else{
            return false;
        }
    }
    //로그인시 인증된 이메일이 맞는지 확인
    public boolean AuthorizationEmail(String email){
        if(checked){
            return true;
        }
        else return false;
    }
    public void makeRandomNumber(){ // 이메일 인증번호 생성 임의의 6자리 양수 반환
        Random r = new Random();
        String randomNumber= "";
        for(int i =0; i<6; i++){
            randomNumber += Integer.toString(r.nextInt(10));
        }

        authNumber = Integer.parseInt(randomNumber);
    }


    //mail을 어디서 보내는지, 어디로 보내는지 , 인증 번호를 html 형식으로 어떻게 보내는지 작성합니다.
    public String joinEmail(String email) {
        makeRandomNumber();
        String setFrom = "heekyoung2000@naver.com"; // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = email;
        String title = "회원 가입 인증 이메일 입니다."; // 이메일 제목
        String content =
                "예약 구매 사이트에 방문해주셔서 갑사합니다." + 	//html 형식으로 작성 !
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "인증번호를 제대로 입력해주세요"; //이메일 내용 삽입
        System.out.println("✅✅✅"+setFrom);
        System.out.println("✅✅✅"+toMail);
        mailSend(setFrom, toMail, title, content);
        return Integer.toString(authNumber);
    }

    //이메일 전송
    private void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content,true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        redisUtil.setDataExpire(Integer.toString(authNumber),toMail,60*60L); //60분으로 늘림
    }

}
