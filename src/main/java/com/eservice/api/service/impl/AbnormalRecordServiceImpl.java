package com.eservice.api.service.impl;

import com.eservice.api.dao.AbnormalRecordMapper;
import com.eservice.api.model.abnormal_record.AbnormalRecord;
import com.eservice.api.model.abnormal_record.AbnormalRecordDetail;
import com.eservice.api.model.abnormal_record.AbnormalRecordStatistics;
import com.eservice.api.service.AbnormalRecordService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2017/12/27.
 */
@Service
@Transactional
public class AbnormalRecordServiceImpl extends AbstractService<AbnormalRecord> implements AbnormalRecordService {
    @Resource
    private AbnormalRecordMapper abnormalRecordMapper;

    public List<AbnormalRecordDetail> selectAbnormalRecordDetails(Integer taskRecordId) {
        return abnormalRecordMapper.selectAbnormalRecordDetails(taskRecordId);
    }

    public List<AbnormalRecordDetail> selectAbnormalRecordDetailList(String nameplate, String orderNum, Integer abnormalType, String taskName, Integer submitUser, Integer solutionUser, Integer finishStatus, String queryStartTime, String queryFinishTime) {
        return abnormalRecordMapper.selectAbnormalRecordDetailList(nameplate, orderNum, abnormalType, taskName, submitUser, solutionUser, finishStatus, queryStartTime, queryFinishTime);
    }

    /**
     * 保存并得到id号
     */
    public void saveAndGetID(AbnormalRecord abnormalRecord) {
        abnormalRecordMapper.saveAndGetID(abnormalRecord);
    }

    public List<AbnormalRecordStatistics> getAbnormalStatistics(Integer mode) {
        return abnormalRecordMapper.getAbnormalStatistics(mode);
    }
}
