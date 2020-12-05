package blog.hazelk.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import blog.hazelk.join.JoinService;
import blog.hazelk.model.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestAPIController {
	private final JoinService joinService;
	private final BCryptPasswordEncoder passwordEncoder;

	@PostMapping("/joinProc")
	public String join(@RequestBody User user) {
		System.out.println("001. join");
		System.out.println("user: " + user);
//		String result = joinService.join(user);
		return null;
	}
}
