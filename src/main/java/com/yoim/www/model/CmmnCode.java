package com.yoim.www.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_cmmn_code")
@IdClass(CmmnCodeId.class)
public class CmmnCode {
	@Id
	@Column(name = "code_ty_id")
	private String codeTyId;
	
	@Id
	@Column(name = "code_id")
	private String codeId;
	
	@Column(name = "code_nm")
	private String codeNm;
	
	@Column(name = "code_eng_nm")
	private String codeEngNm;
	
	@Column(name = "code_dc")
	private String codeDc;
	
	@Column(name = "code_sn")
	private int codeSn;
	
	@Column(name = "use_at", columnDefinition = "CHAR(1)")
	private String useAt;
	
	@Column(name = "regist_id")
	private String registId;
	
	@Column(name = "regist_dt")
	private LocalDateTime registDt;
	
	@Column(name = "updusr_id")
	private String updusrId;
	
	@Column(name = "updt_dt")
	private LocalDateTime updtDt;
}
