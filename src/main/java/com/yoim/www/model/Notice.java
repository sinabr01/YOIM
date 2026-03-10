package com.yoim.www.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_notice")
public class Notice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notice_id")
	private Long noticeId;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "content", columnDefinition = "MEDIUMTEXT")
	private String content;
	
	@Column(name = "pinned_yn", length = 1, columnDefinition = "ENUM('Y','N')")
	private String pinnedYn;
	
	@Column(name = "use_yn", length = 1, columnDefinition = "ENUM('Y','N')")
	private String useYn;
	
	@Column(name = "regist_id")
	private String registId;
	
	@Column(name = "regist_dt")
	private LocalDateTime registDt;
	
	@Column(name = "updusr_id")
	private String updusrId;
	
	@Column(name = "updt_dt")
	private LocalDateTime updtDt;
}
