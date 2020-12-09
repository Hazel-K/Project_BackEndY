package blog.hazelk.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class User{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String username; // 아이디
	private String password; // 비밀번호
	private String roles; // 권한 - USER,ADMIN
	
	private String nickname; // 별명
	private String fullname; // 이름
	private String zoneCode; // 우편번호
	private String address; // 주소
	private String addrDetail; // 상세주소
	private String idNumber; // 주민번호
	private String phone; // 휴대폰번호
	private String email; // 이메일주소
	
	public List<String> getRoleList() {
		if(this.roles.length() > 0) {
			return Arrays.asList(this.roles.split(",")); // ,기준으로 다중 role을 구분
		}
		return new ArrayList<>();
	}
}
