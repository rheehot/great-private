package com.ssafy.great.controller;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.great.dto.User;
import com.ssafy.great.model.service.UserService;
import com.ssafy.great.util.RestUtil;

@RestController

public class SendEmailController {

	private static final Logger logger = LoggerFactory.getLogger(SendEmailController.class);

	@Autowired
	JavaMailSender jms;
	@Autowired
	UserService userService;
	
	@GetMapping(value="/searchpassword/{email}")
	public ResponseEntity<Map<String,Object>> searchPassword(@PathVariable String email) {
		User user = userService.selectByEmail(email);
		if(user!=null) {
			
			MimeMessage msg = jms.createMimeMessage();
			try {
				
				msg.setSubject("grEAT 비밀번호 찾기 : ");
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));	
				
				msg.setText("비밀번호 : "+ user.getPassword());
				msg.setSentDate(new Date());
				jms.send(msg);
			}catch(Exception e) {
				System.out.println(e);
			}
			
			
			
			return RestUtil.handleSuccess("Sueecess");
			
		}
		else {
			return RestUtil.handleSuccess("Fail");
		}
	}
	
	
	
	
	
	@GetMapping(value="/sendemail/{email}")
	public ResponseEntity<Map<String,Object>> sendEmail(@PathVariable String email) {
		StringBuffer buf =new StringBuffer();
		MimeMessage msg = jms.createMimeMessage();
			try {
				
				msg.setSubject("회원가입 인증 이메일");
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
				Random rnd =new Random();

			

				for(int i=0;i<10;i++){
				    if(rnd.nextBoolean()){
				        buf.append((char)((int)(rnd.nextInt(26))+97));
				    }else{
				        buf.append((rnd.nextInt(10)));
				    }

				}



				msg.setText("인증번호 : "+buf.toString());
				msg.setSentDate(new Date());
				jms.send(msg);
			}catch(Exception e) {
				System.out.println(e);
			}

		
		
		return RestUtil.handleSuccess(buf.toString());
	}

}
