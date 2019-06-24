package com.controllers;

import com.models.UserVO;
import com.repositories.UserRepository;
import com.models.SwinStatus;
import com.repositories.SwinStatusRepository;
import com.services.MongoUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Principal;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	SwinStatusRepository swinStatusRepository;

	@Autowired
	MongoUserDetailsService userService;

	@Autowired
	public JavaMailSender emailSender;

	public String nowTime() {
		Date now = new Date();
		TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss] ");
		sdf.setTimeZone(tz);
		String time = sdf.format(now);

		return time;
	}

	@RequestMapping("/register.html")
	public String register() {
		return "register";
	}

	@RequestMapping("/addAccount")
	public String addAccount(@RequestParam String userName, @RequestParam String userId, @RequestParam String userPw, @RequestParam String confirmPw) throws Exception {
		UserVO user = new UserVO();
		if(userRepository.findByuserId(userId) == null) {
			if(userPw.equals(confirmPw)) {
				user.setUserName(userName);
				user.setUserId(userId);
				user.setUserPw(userPw);
				userService.saveUser(user);
			} else {
				return "redirect:/register.html?pwMatch";
			}
		} else {
			return "redirect:/register.html?exist";
		}
		return "redirect:/login.html";
	}

	@RequestMapping("")
	public String home() {
		return "redirect:/index.html";
	}

	@RequestMapping("/login.html")
	public String login() {
		HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        	String ip = req.getHeader("X-FORWARDED-FOR");
        	if (ip == null)
            		ip = req.getRemoteAddr();
		System.out.println("> " + nowTime() + ip + ": Try to access");		
		return "login";
	}

	@RequestMapping("/forgot-password.html")
	public String forgotPW() {
		return "forgot-password";
	}

	@RequestMapping("/mailSender")
	public String mailSender(@RequestParam String email) {
		SimpleMailMessage message = new SimpleMailMessage();
		if(userRepository.findByuserId(email) != null) {
			UserVO user = userRepository.findByuserId(email);
			String uuid = UUID.randomUUID().toString().replaceAll("-","");
			uuid = uuid.substring(0,8);
			user.setUserPw(uuid);
			String body = user.getUserName() + " 님의 임시 비밀번호 : " + uuid;
			userService.saveUser(user);

			message.setTo(email);
			message.setSubject("Swin 임시 비밀번호 안내");
			message.setText(body);
			emailSender.send(message);
		} else {
			return "redirect:/login.html?notexist";
		}
		return "redirect:/login.html";
	}

	@RequestMapping("/information.html")
	public String info(Model model, Principal principal) {
		model.addAttribute("userinfo", userRepository.findByuserId(principal.getName()));
		model.addAttribute("swinlist", swinStatusRepository.findByuserId(principal.getName()));
		return "information";
	}

	@RequestMapping("/check-password.html")
	public String checkPassword() {
		return "check-password";
	}

	@RequestMapping("/pwCheck")
	public String pwCheck(@RequestParam String userPw, Principal principal) {
		UserVO user = userRepository.findByuserId(principal.getName());
		if(userService.matchPw(userPw, user.getUserPw())) {
			return "redirect:/edit-password.html";
		} else {
			return "redirect:/check-password.html?error";
		}
	}

	@RequestMapping("/edit-password.html")
	public String editPassword() {
		return "edit-password";
	}

	@RequestMapping("/pwEdit")
	public String pwEdit(@RequestParam String userPw, @RequestParam String confirmPw, Principal principal) {
		UserVO user = userRepository.findByuserId(principal.getName());
		if(userPw.equals(confirmPw)) {
			user.setUserPw(userPw);
			userService.saveUser(user);

			return "redirect:/information.html?success";
		} else {
			return "redirect:/edit-password.html?match";
		}
	}

	@RequestMapping("/withdrawal")
	public String withdrawal(Principal principal) {
		userRepository.deleteByuserId(principal.getName());
		swinStatusRepository.deleteByuserId(principal.getName());
		return "redirect:/logout";
	}

}

