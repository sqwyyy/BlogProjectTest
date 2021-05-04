package com.Controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pojo.KeyWords;
import com.pojo.Report;
import com.pojo.ReportArticle;
import com.pojo.paging;
import com.result.Result;
import com.result.ResultFactory;
import com.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @date 2021/4/11 - 12:58
 */
@RestController
@CrossOrigin
public class ReportController {
    @Autowired
    ReportService reportService;


    @GetMapping("/api/admin/report/{pageSize}/{page}")
    public Result listReport(@PathVariable("page") int page, @PathVariable("pageSize") int size) {
        Page p = PageHelper.startPage(page , size);
        PageInfo<ReportArticle> pageInfo = new PageInfo<>(reportService.listForAll(page,size));
        return ResultFactory.buildSuccessResult(new paging((List<ReportArticle>) pageInfo.getList(),pageInfo.getTotal()));
    }



    @PostMapping("api/admin/reportAdd/{Aid}")
    public Result addReport(@RequestBody Report report,@PathVariable("Aid") Integer Aid,HttpServletRequest httpServletRequest){
        reportService.addReport(report.getContent(),Aid,httpServletRequest);
        return ResultFactory.buildSuccessResult("添加成功");
    }

    @DeleteMapping("api/admin/reportDelete/{id}")
    public Result delete(@RequestBody Report report){
        reportService.deleteReport(report.getContent());
        return ResultFactory.buildSuccessResult("删除成功");
    }


}
