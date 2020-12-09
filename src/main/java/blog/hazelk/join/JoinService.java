package blog.hazelk.join;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import blog.hazelk.model.User;
import blog.hazelk.repository.UserRepository;

@Service
public class JoinService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public String join(User user) {
		System.out.println(user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles("ROLE_USER");
		try {
			userRepository.save(user);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
	}
}
