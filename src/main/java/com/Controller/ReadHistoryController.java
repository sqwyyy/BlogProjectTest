package com.Controller;

import com.dao.ReadHistoryMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pojo.KeyWords;
import com.pojo.ReadHistory;
import com.pojo.paging;
import com.result.Result;
import com.result.ResultFactory;
import com.service.ReadHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @date 2021/4/27 - 21:14
 */
@CrossOrigin
@RestController
public class ReadHistoryController {
    @Autowired
    ReadHistoryService historyService;

    @Autowired
    ReadHistoryMapper readHistoryMapper;

    @GetMapping("/api/admin/history/{pageSize}/{page}")
    public Result listArticles(@PathVariable("page") int page, @PathVariable("pageSize") int size,HttpServletRequest httpServletRequest) {
        Page p = PageHelper.startPage(page , size);
        PageInfo<ReadHistory> pageInfo = new PageInfo<>(historyService.list(page,size,httpServletRequest));
        return ResultFactory.buildSuccessResult(new paging((List<ReadHistory>) pageInfo.getList(),pageInfo.getTotal()));
    }

    @PostMapping("api/admin/historyADD/{articleId}")
    public Result add(@PathVariable("articleId") Integer articleId, HttpServletRequest httpServletRequest){
        historyService.add(articleId,httpServletRequest);
        return ResultFactory.buildSuccessResult("添加成功");
    }

    @DeleteMapping("api/admin/historyDelete/{pid}")
    public Result delete(@PathVariable("pid") Integer pid){
        readHistoryMapper.deleteById(pid);
        return ResultFactory.buildSuccessResult("删除成功");
    }
}
