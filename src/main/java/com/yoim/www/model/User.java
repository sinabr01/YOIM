package com.yoim.www.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_user")
public class User {
	@Id
	@Column(name = "user_id", columnDefinition = "BIGINT")
	private Long userId;
	
	@Column(name = "login_id", columnDefinition = "TEXT")
	private String loginId;
	
	@Column(name = "login_pw", columnDefinition = "TEXT")
	private String loginPw;
	
	@Column(name = "user_nm", columnDefinition = "TEXT")
	private String userNm;
	
	@Column(name = "nick_nm", columnDefinition = "TEXT")
	private String nickNm;
	
	@Column(name = "email", columnDefinition = "TEXT")
	private String email;
	
	@Column(name = "birth_date", columnDefinition = "TEXT")
	private String birthDate;
	
	@Column(name = "gender", columnDefinition = "TEXT")
	private String gender;
	
	@Column(name = "intro", columnDefinition = "TEXT")
	private String intro;
	
	@Column(name = "interests", columnDefinition = "TEXT")
	private String interests;
	
	@Column(name = "phone", columnDefinition = "TEXT")
	private String phone;
	
	@Column(name = "provider_id", columnDefinition = "TEXT")
	private String providerId;
	
	@Column(name = "provider_type", columnDefinition = "TEXT")
	private String providerType;
	
	@Column(name = "user_del_yn", columnDefinition = "TEXT")
	private String userDelYn;
	
	@Column(name = "user_del_date")
	private LocalDateTime userDelDate;
	
	@Column(name = "user_img_id", columnDefinition = "BIGINT")
	private Long userImgId;
	
	@Column(name = "user_type", columnDefinition = "TEXT")
	private String userType;
	
	@Column(name = "regist_id", columnDefinition = "TEXT")
	private String registId;
	
	@Column(name = "regist_dt")
	private LocalDateTime registDt;
	
	@Column(name = "updusr_id")
	private String updusrId;
	
	@Column(name = "updt_dt")
	private LocalDateTime updtDt;
}
