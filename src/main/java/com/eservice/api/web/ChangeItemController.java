package com.eservice.api.web;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.change_item.ChangeItem;
import com.eservice.api.service.ChangeItemService;
import com.eservice.api.service.impl.ChangeItemServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Class Description: xxx
* @author Wilson Hu
* @date 2019/11/05.
*/
@RestController
@RequestMapping("/change/item")
public class ChangeItemController {
    @Resource
    private ChangeItemServiceImpl changeItemService;

    @PostMapping("/add")
    public Result add(ChangeItem changeItem) {
        changeItemService.save(changeItem);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        changeItemService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(ChangeItem changeItem) {
        changeItemService.update(changeItem);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        ChangeItem changeItem = changeItemService.findById(id);
        return ResultGenerator.genSuccessResult(changeItem);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ChangeItem> list = changeItemService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据联系单id查找该联系单的所有变更条目
     * @param page
     * @param size
     * @param contactFormId
     * @return
     */
    @PostMapping("/selectChangeItemList")
    public Result selectChangeItemList(@RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(defaultValue = "0") Integer size,
                                 @RequestParam Integer contactFormId) {
        PageHelper.startPage(page, size);
        List<ChangeItem> list = changeItemService.selectChangeItemList(contactFormId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
