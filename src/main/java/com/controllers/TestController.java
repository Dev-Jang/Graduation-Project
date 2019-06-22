package com.controllers;

import com.models.UserVO;
import com.repositories.UserRepository;
import com.models.SwinStatus;
import com.repositories.SwinStatusRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.security.Principal;

@RestController
public class TestController {

	@Autowired
	private UserRepository userRepositoryTest;
	@Autowired
	private SwinStatusRepository swinStatusRepositoryTest;

	public TestController() {}

	@RequestMapping(value = "/test/user", method = RequestMethod.GET)
	public String getAllUser() {
		long cntL = userRepositoryTest.count();
		int cntI = (int)cntL;
		List<UserVO> users = userRepositoryTest.findAll();

		String result = "";

		for(int i=0; i<cntI; i++) {
			result += " / " + users.get(i).getUserId();
		}
		return result;
	}

	@RequestMapping(value = "/test/user/{userId}")
	public String getUserById(@PathVariable("userId") String userId) {
		String inputId = userId;
		UserVO documentId = userRepositoryTest.findByuserId(userId);
		String checkId = null;
		if(documentId == null) {
			return "null";
		} else {
			checkId = documentId.getUserId();
		}
		return checkId;
	}

	@RequestMapping(value = "/test/swin", method = RequestMethod.GET)
	public List<SwinStatus> getAllSwin() {
		return swinStatusRepositoryTest.findAll();
	}

	@RequestMapping(value = "/test/test", method = RequestMethod.GET)
	public String testing(Principal principal) {
		return principal.getName();
	}

	@RequestMapping(value = "/json/{swinIp}", method = {RequestMethod.GET, RequestMethod.POST}, produces = {"application/json"})
	public @ResponseBody Map<String, Object> jsonTest(@PathVariable("swinIp") String swinIp) {
		Map<String, Object> retVal = new HashMap<String, Object>();
		SwinStatus swin = swinStatusRepositoryTest.findByswinIp(swinIp);
		boolean open = swin.getOpen();
		boolean blind = swin.getBlind();
		if(!open) {
			retVal.put("motor", 0);
		} else {
			retVal.put("motor", 1);
		}
		if(!blind) {
			retVal.put("film", 0);
		} else {
			retVal.put("film", 1);
		}
		return retVal;
	}

}
