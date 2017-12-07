package com.eservice.api.service.impl;

import com.eservice.api.dao.ContractMapper;
import com.eservice.api.model.contract.Contract;
import com.eservice.api.service.ContractService;
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
public class ContractServiceImpl extends AbstractService<Contract> implements ContractService {
    @Resource
    private ContractMapper contractMapper;

}
