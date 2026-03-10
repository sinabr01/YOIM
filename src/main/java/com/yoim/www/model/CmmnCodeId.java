package com.yoim.www.model;

import lombok.Data;
import java.io.Serializable;

@Data
public class CmmnCodeId implements Serializable {
    private String codeTyId;
    private String codeId;

    public CmmnCodeId() {}

    public CmmnCodeId(String codeTyId, String codeId) {
        this.codeTyId = codeTyId;
        this.codeId = codeId;
    }
}
