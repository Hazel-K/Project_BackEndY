package blog.hazelk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import blog.hazelk.join.JoinService;
import blog.hazelk.login.LoginService;
import blog.hazelk.model.User;

@RestController
public class RestAPIController {
	@Autowired
	private LoginService loginService;
	@Autowired
	private JoinService joinService;

	@PostMapping("join")
	public String join(@RequestBody User user) {
		System.out.println("001. join");
		System.out.println("user: " + user);
		String result = joinService.join(user);
		return result;
	}
	
	@PostMapping("login")
	public String login() {
		System.out.println("002. login");
		return "<h1>login<h1>";
	}
}
