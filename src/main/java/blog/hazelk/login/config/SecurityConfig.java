package blog.hazelk.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;

import blog.hazelk.login.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final CorsFilter corsFilter;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable(); // csrf 보호 비활성화
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않음
		.and()
		.addFilter(corsFilter) // CrossOrigin에 인증하는 게 아니라 시큐리티 필터에서 등록 인증
		.formLogin().disable() // 로그인 폼 안쓴다
		.httpBasic().disable() // HTTP 기반 인증 안쓴다
		.addFilter(new JwtAuthenticationFilter(authenticationManager())) // 별도의 JWT 필터를 사용하겠다. 얘 사용 시 AuthenticationManager를 파라미터로 줘야 함
		.authorizeRequests() // 권한 요청을 할 건데,
		.antMatchers("/api/admin/**") // /api/admin/.. 으로 호출하는 모든 것들의 권한은
		.access("hasRole('ROLE_ADMIN')") // ROLE_ADMIN이 있어야 호출 가능
		.antMatchers("/api/user/**") // /api/user/.. 로 호출하는 모든 것들의 권한은
		.access("hasRole('ROLE_USER')") // ROLE_USER가 있어야 호출 가능
		.anyRequest() // 나머지 요청들은
		.permitAll() // 전부 허가
		;
	}
}
