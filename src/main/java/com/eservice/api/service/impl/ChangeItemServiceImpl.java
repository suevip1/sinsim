package com.eservice.api.service.impl;

import com.eservice.api.dao.ChangeItemMapper;
import com.eservice.api.model.change_item.ChangeItem;
import com.eservice.api.service.ChangeItemService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2019/11/05.
*/
@Service
@Transactional
public class ChangeItemServiceImpl extends AbstractService<ChangeItem> implements ChangeItemService {
    @Resource
    private ChangeItemMapper changeItemMapper;

}
