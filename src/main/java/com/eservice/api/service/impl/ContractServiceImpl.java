package com.eservice.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eservice.api.dao.ContractMapper;
import com.eservice.api.model.contract.Contract;
import com.eservice.api.model.contract.ContractDetail;
import com.eservice.api.model.contract_sign.ContractSign;
import com.eservice.api.model.contract_sign.SignContentItem;
import com.eservice.api.model.order_sign.OrderSign;
import com.eservice.api.model.role.Role;
import com.eservice.api.service.ContractService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


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

    public List<ContractDetail> selectContracts() {
        return contractMapper.selectContracts();
    }

    public void saveAndGetID(Contract contract) {
        contractMapper.saveAndGetID(contract);
    }

}
