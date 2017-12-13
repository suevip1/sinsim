package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.contract.Contract;
import com.eservice.api.model.contract.ContractDetail;

import java.util.List;

public interface ContractMapper extends Mapper<Contract> {
    List<ContractDetail> selectContracts();
}