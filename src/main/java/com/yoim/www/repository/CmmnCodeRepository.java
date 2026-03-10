package com.yoim.www.repository;

import com.yoim.www.model.CmmnCode;
import com.yoim.www.model.CmmnCodeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmmnCodeRepository extends JpaRepository<CmmnCode, CmmnCodeId> {

    List<CmmnCode> findByCodeTyIdOrderByCodeSnAsc(String codeTyId);
}
