package com.yoim.www.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Company {
	private Long companyId;          // BIGINT → Long
	private Long ownerUserId;        // BIGINT → Long
	private String companyCode;       // VARCHAR
	private String companyName;       // VARCHAR
	private String companyType;       // VARCHAR
	private String bizRegNo;          // VARCHAR
	private String repName;           // VARCHAR
	private String tel;               // VARCHAR
	private String email;             // VARCHAR
	private String description;       // TEXT
	private String category;          // VARCHAR
	private String tags;              // VARCHAR
	private String city;              // VARCHAR
	private String district;          // VARCHAR
	private String status;            // VARCHAR
	private String verifiedYn;        // CHAR(1)
	private String profileImageUrl;   // VARCHAR
	private String bannerImageUrl;    // VARCHAR
	private BigDecimal defaultFeeRate; // DECIMAL(5,2) → BigDecimal
	private String taxType;           // VARCHAR
	private String payoutCycle;       // VARCHAR
	private Integer payoutDay;        // TINYINT → Integer
	private Long defaultAccountId;    // BIGINT → Long
	private String invoiceEmail;      // VARCHAR
	private BigDecimal ratingAvg;     // DECIMAL(3,2) → BigDecimal
	private Integer ratingCount;      // INT → Integer
	private Integer meetingCount;     // INT → Integer
	private Integer followerCount;    // INT → Integer
	private String termsVersion;      // VARCHAR
	private String marketingOptIn;    // CHAR(1)
	private String notifyEmailYn;     // CHAR(1)
	private String notifySmsYn;       // CHAR(1)
	private String registId;          // VARCHAR
	private LocalDateTime registDt;   // TIMESTAMP → LocalDateTime
	private String updusrId;          // VARCHAR
	private LocalDateTime updtDt;     // TIMESTAMP → LocalDateTime
	private String deletedYn;         // CHAR(1)

}
