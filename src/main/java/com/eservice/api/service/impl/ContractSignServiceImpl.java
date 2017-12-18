package com.eservice.api.service.impl;

import com.eservice.api.dao.ContractSignMapper;
import com.eservice.api.model.contract_sign.ContractSign;
import com.eservice.api.service.ContractSignService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/12/07.
*/
@Service
@Transactional
public class ContractSignServiceImpl extends AbstractService<ContractSign> implements ContractSignService {
    @Resource
    private ContractSignMapper contractSignMapper;

    public ContractSign detailByContractId(String contractId) {
        return contractSignMapper.detailByContractId(contractId);
    }

}
