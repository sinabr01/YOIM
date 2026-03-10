package com.yoim.www.serviceImpl;

import com.yoim.www.model.CmmnCode;
import com.yoim.www.repository.CmmnCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmmnCodeService {

	@Autowired
	CmmnCodeRepository cmmnCodeRepository;

	public List<CmmnCode> cmmnCodeSelect(String codeTyId) {
		return cmmnCodeRepository.findByCodeTyIdOrderByCodeSnAsc(codeTyId);
	}

}
