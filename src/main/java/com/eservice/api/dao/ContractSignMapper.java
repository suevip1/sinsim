package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.contract_sign.ContractSign;
import org.apache.ibatis.annotations.Param;

public interface ContractSignMapper extends Mapper<ContractSign> {

    ContractSign detailByContractId(@Param("contractId") String contractId);
}