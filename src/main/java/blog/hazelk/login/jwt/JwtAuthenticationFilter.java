package blog.hazelk.login.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import blog.hazelk.login.auth.PrincipalDetails;
import blog.hazelk.model.User;
import blog.hazelk.variable.JWTProperties;
import lombok.RequiredArgsConstructor;

/*
 * 스프링 시큐리티에 있는 UsernamePasswordAuthenticationFilter 는 
 * /login POST 요청시 동작하지만, 현재는 FormLogin을 비활성화해서 동작하지 않음
 * 별도로 등록하기 위해 SecurityConfig에 addFilter로 등록
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	
	// /login 요청을 하면 로그인 시도를 위해 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter : 로그인 시도중");
		// username, password 받아서 정상인지 로그인 시도 해보기
		try {
			ObjectMapper om = new ObjectMapper(); // 이친구가 JSON객체를 parsing해줌
			User user = om.readValue(request.getInputStream(), User.class); // 유저 Obj에 JSON 객체 담아줌
			System.out.println("11. " + user);
			
			// 이제 토큰 만들어서 로그인 시도. FormLogin을 사용한다면 자동으로 해주는 부분임
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			Authentication authentication = authenticationManager.authenticate(authenticationToken); // 이것이 실행되면 PrincipalDetailsService의 loadUserByUsername()이 실행됨
			// 그리고 authentication에는 내 로그인한 정보가 담기게 됨
			// 인증이 정상적으로 완료되면 authentication 객체는 session에 저장됨 (로그인이 정상적으로 됐다는 뜻)
			PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal(); // authentication 로그인 정보 받기
			System.out.println("13. " + principalDetails.getUsername());
			return authentication; // 세션에 저장
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("=======================================");
		// authenticationManager를 사용해서 로그인 시도를 하면 PrincipalDetailsService가 호출되고, loadByUsername 함수가 실행됨
		// 이후 PrincipalDetails를 세션에 담고 (안담으면 권한 관리가 안됨) JWT 토큰을 만들어서 응답해주면 됨
//		return super.attemptAuthentication(request, response); // 구현중에 오류 안내기 위해 부모의 결과 호출
		return null;
	}
	
	// 위에 attemptAuthentication 함수 성공하면 실행되는 함수
	// JWT 토큰 만들어서 request 요청한 사용자에게 토큰을 response 하면 됨
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("14. successfulAuthentication 실행됨");
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();
		String jwtToken = JWT.create()
				.withSubject("LoginToken") // 토큰의 이름
				.withExpiresAt(new Date(System.currentTimeMillis() + JWTProperties.EXPIRATION_TIME)) // 토큰의 유효시간
				.withClaim("id", principalDetails.getUser().getId()) // 토큰 필수 요소
				.withClaim("username", principalDetails.getUsername()) // 토큰 필수 요소
				.sign(Algorithm.HMAC512(JWTProperties.SECRET)); // 토큰 사인
		
		response.addHeader("Authorization", "Bearer " + jwtToken); // 토큰을 발급
	}
	
}
