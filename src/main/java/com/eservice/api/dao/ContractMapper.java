package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.contract.Contract;
import com.eservice.api.model.contract.ContractDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContractMapper extends Mapper<Contract> {
    List<ContractDetail> selectContracts(@Param("contract_num") String contractNum,
                                         @Param("status") Integer status,
                                         @Param("sellman") String sellman,
                                         @Param("record_user") String recordUser,
                                         @Param("role_name") String roleName,
                                         @Param("market_group_name") String marketGroupName,
                                         @Param("query_start_time") String query_start_time,
                                         @Param("query_finish_time") String query_finish_time);

    List<ContractDetail> selectContractsByFuzzy(@Param("contract_num") String contractNum,
                                                @Param("status") Integer status,
                                                @Param("sellman") String sellman,
                                                @Param("record_user") String recordUser,
                                                @Param("role_name") String roleName,
                                                @Param("market_group_name") String marketGroupName,
                                                @Param("query_start_time") String query_start_time,
                                                @Param("query_finish_time") String query_finish_time );

    List<ContractDetail> selectContractsByFuzzyAndDomestic(@Param("contract_num") String contractNum,
                                                @Param("status") Integer status,
                                                @Param("sellman") String sellman,
                                                @Param("record_user") String recordUser,
                                                @Param("role_name") String roleName,
                                                @Param("market_group_name") String marketGroupName,
                                                @Param("query_start_time") String query_start_time,
                                                @Param("query_finish_time") String query_finish_time,
                                                @Param("domesticTradeArr") String[] domesticTradeArr);
    void saveAndGetID(Contract contract);

    //selectAllCustomer
    List<ContractDetail> selectAllCustomer(@Param("name") String name);
	
    Contract getContractByOrderNumber(@Param("orderNumber") String orderNumber);
}