package com.yoim.www.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_user_auth")
public class UserAuth {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_auth_id")
	private Long userAuthId;
	
	@Column(name = "provider_id")
	private String providerId;
	
	@Column(name = "provider_type")
	private String providerType;
	
	@Column(name = "provider_name")
	private String providerName;
	
	@Column(name = "regist_id")
	private String registId;
	
	@Column(name = "regist_dt")
	private LocalDateTime registDt;
	
	@Column(name = "updusr_id")
	private String updusrId;
	
	@Column(name = "updt_dt")
	private LocalDateTime updtDt;
}
