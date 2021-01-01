package blog.hazelk.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;

import blog.hazelk.model.User;
import blog.hazelk.repository.UserRepository;
import blog.hazelk.variable.Common;
import blog.hazelk.variable.Secret;

@Service
public class JwtService {
	@Autowired
	private UserRepository userRepository;

	public String getUserInfo(String payload) {
		Gson gson = new Gson();
		
		// JWT 토큰을 검증해서 정상적인 사용자인지 확인
		String jwtToken = payload.replace(Common.BEARER, "");

		// 토큰을 풀어 ID값을 추출
		String username = JWT.require(Algorithm.HMAC512(Secret.JWTSECRET)).build().verify(jwtToken).getClaim(Common.USERNAME).asString();

		// 서명이 정상적으로 됐을 경우
		if (username != null) {
			User userEntity = userRepository.findByUsername(username);
			userEntity.setPassword("");
			System.out.println("21. " + userEntity);
			return gson.toJson(userEntity);
		}
		return "0";
	}

}
