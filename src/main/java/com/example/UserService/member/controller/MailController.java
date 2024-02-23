package com.example.UserService.member.controller;

import com.example.UserService.member.Service.MailSendService;
import com.example.UserService.member.dto.EmailCheckDTO;
import com.example.UserService.member.dto.EmailRequestDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/UserService")
public class MailController {
    private final MailSendService mailService;

    public MailController(MailSendService mailService) {
        this.mailService = mailService;
    }

    @PostMapping ("/mailSend")
    public String mailSend(@RequestBody @Valid EmailRequestDTO emailDto){
        System.out.println("이메일 인증 요청이 들어옴");
        System.out.println("이메일 인증 이메일 :"+emailDto.getEmail());
        return mailService.joinEmail(emailDto.getEmail());
    }
    @PostMapping("/authCheck")
    public String AuthCheck(@RequestBody @Valid EmailCheckDTO emailCheckDto){
        System.out.println(emailCheckDto.getAuthNum());
        System.out.println(emailCheckDto.getEmail());
        Boolean Checked=mailService.CheckAuthNum(emailCheckDto.getEmail(),emailCheckDto.getAuthNum());
        if(Checked){
            return "인증되었습니다.";
        }
        else{
            System.out.println(emailCheckDto.getAuthNum());
            throw new NullPointerException("인증이 잘못되었습니다!");
        }
    }
}
