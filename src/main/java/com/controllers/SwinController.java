package com.controllers;

import com.models.SwinStatus;
import com.repositories.SwinStatusRepository;
import com.models.UserVO;
import com.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class SwinController {

	@Autowired
	SwinStatusRepository swinStatusRepository;

	@Autowired
	UserRepository userRepository;

	@RequestMapping("/main.html")
	public String main(Model model, Principal principal) {
		model.addAttribute("swinList", swinStatusRepository.findByuserId(principal.getName()));
		model.addAttribute("swinCount", swinStatusRepository.countByuserId(principal.getName()));
		return "main";
	}

	@RequestMapping("/prodregister.html")
	public String prodregister() {
		return "prodregister";
	}

	@RequestMapping("/addswin")
	public String main(@RequestParam("swinIp") String swinIp, @RequestParam("swinName") String swinName, Principal principal) {
		SwinStatus swinStat = new SwinStatus();
		if(swinStatusRepository.findByswinIp(swinIp) == null) {
			swinStat.setSwinIp(swinIp);
			swinStat.setSwinName(swinName);
			swinStat.setRainSensor(0);
			swinStat.setIlluSensor(0);
			swinStat.setOpen(false);
			swinStat.setBlind(false);
			swinStat.setUserId(principal.getName());
			swinStatusRepository.save(swinStat);
		} else {
			    return "redirect:/prodregister.html?exist";
		}

		return "redirect:/prodregister.html";
	}

	@RequestMapping("/window.html")
	public String swin(@RequestParam("swinIp") String swinIp, Model model) {
		model.addAttribute("current", swinStatusRepository.findByswinIp(swinIp));
		return "window";
	}

	@RequestMapping(value="/control", method=RequestMethod.POST)
	public String control(@RequestParam("swinIp") String swinIp, @RequestParam("swinName") String swinName, @RequestParam("rainSensor") int rainSensor, @RequestParam("illuSensor") int illuSensor, @RequestParam("open") boolean open, @RequestParam("blind") boolean blind, Principal principal, RedirectAttributes redirect) {
		SwinStatus swinStat = swinStatusRepository.findByswinIp(swinIp);
		swinStat.setSwinIp(swinIp);
		swinStat.setSwinName(swinName);
		swinStat.setRainSensor(rainSensor);
		swinStat.setIlluSensor(illuSensor);
		swinStat.setOpen(open);
		swinStat.setBlind(blind);
		swinStat.setUserId(principal.getName());
		swinStatusRepository.save(swinStat);

		redirect.addAttribute("swinIp", swinIp);
		return "redirect:/window.html";
	}

	@RequestMapping("/deletion")
	public String deletion(@RequestParam("swinIp") String swinIp) {
		swinStatusRepository.deleteByswinIp(swinIp);
		return "redirect:/index.html";
	}

}
