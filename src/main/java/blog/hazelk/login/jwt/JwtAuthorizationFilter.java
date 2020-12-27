package blog.hazelk.login.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import blog.hazelk.login.auth.PrincipalDetails;
import blog.hazelk.model.User;
import blog.hazelk.repository.UserRepository;
import blog.hazelk.variable.Common;
import blog.hazelk.variable.Secret;

/*
 * 시큐리티가 filter를 가지고 있는데, 그 중 BasicAuthentication이 있음.
 * 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어 있음.
 * 만약 권한이나 인증이 필요한 주소가 아니라면 이 필터를 이용하지 않음
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{
	private UserRepository userRepository;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}
	
	// 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 됨.
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("15. 인증이나 권한이 필요한 주소 요청이 됨");
		
		String jwtHeader = request.getHeader(Common.AUTHORIZATION);
		System.out.println("15_1. " + jwtHeader);

		// Header가 있는지를 확인, 없으면 그냥 넘김
		if(jwtHeader == null || !jwtHeader.startsWith(Common.BEARER)) {
			chain.doFilter(request, response);
			return;
		}
		
		// JWT 토큰을 검증해서 정상적인 사용자인지 확인
		String jwtToken = jwtHeader.replace(Common.BEARER, "");
		
		// 토큰을 풀어 ID값을 추출
		String username = JWT.require(Algorithm.HMAC512(Secret.JWTSECRET)).build().verify(jwtToken).getClaim(Common.USERNAME).asString();
		
		// 서명이 정상적으로 됐을 경우
		if(username != null) {
			User userEntity = userRepository.findByUsername(username);
			PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
			
			// 정상적 경로인 authenticate 메소드가 아닌 JWT 서명을 통해 만들어진 Authentication 객체
			Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
			
			// 강제로 시큐리티 세션에 접근해서 Authentication 객체를 저장.
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(request, response);
		}
	}
}
