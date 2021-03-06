package blog.hazelk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import blog.hazelk.join.JoinService;
import blog.hazelk.jwt.JwtService;
import blog.hazelk.model.User;

@RestController
public class RestAPIController {
	@Autowired
	private JoinService joinService;
	
	@Autowired
	private JwtService jwtService;

	@PostMapping("/joinProc")
	public String join(@RequestBody User user) {
		String result = joinService.join(user);
		return result;
	}
	
	@GetMapping("/getUser")
	public String getUser(String payload) {
		String result = jwtService.getUserInfo(payload);
		return result;
	}
	
	@GetMapping("/user")
	public String user() {
		return "user";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
}
