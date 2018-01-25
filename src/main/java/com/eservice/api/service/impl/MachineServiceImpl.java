package com.eservice.api.service.impl;

import com.eservice.api.dao.MachineMapper;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine.MachinePlan;
import com.eservice.api.service.MachineService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/12/26.
*/
@Service
@Transactional
public class MachineServiceImpl extends AbstractService<Machine> implements MachineService {
    @Resource
    private MachineMapper machineMapper;
    public Machine searchMachineByAbnormalRecordId( Integer abnormalRecordId ){
        return machineMapper.searchMachineByAbnormalRecordId(abnormalRecordId);
    }
    public Machine searchMachineByTaskQualityRecordId( Integer taskQualityRecordId ){
        return machineMapper.searchMachineByTaskQualityRecordId(taskQualityRecordId);
    }

    public List<Machine> selectMachines(Integer id,
                                        Integer order_id,
                                        String machine_id,
                                        String nameplate,
                                        String location,
                                        Byte status,
                                        Integer machine_type,
                                        String query_start_time,
                                        String query_finish_time,
                                        Boolean is_fuzzy) {
        if(is_fuzzy) {
            return machineMapper.selectMachinesFuzzy(id, order_id, machine_id, nameplate, location, status, machine_type, query_start_time, query_finish_time);
        } else {
            return machineMapper.selectMachines(id, order_id, machine_id, nameplate, location, status, machine_type, query_start_time, query_finish_time);
        }
    }

    public  List<MachinePlan> selectPlanningMachines(Integer order_id,
                                                     String machine_id,
                                                     String nameplate,
                                                     String location,
                                                     Byte status,
                                                     Integer machine_type,
                                                     String query_start_time,
                                                     String query_finish_time,
                                                     Boolean is_fuzzy) {
        if(is_fuzzy) {
            return machineMapper.selectPlanningMachinesFuzzy(order_id, machine_id, nameplate, location, status, machine_type, query_start_time, query_finish_time);
        } else {
            return machineMapper.selectPlanningMachines(order_id, machine_id, nameplate, location, status, machine_type, query_start_time, query_finish_time);
        }

    }
}
