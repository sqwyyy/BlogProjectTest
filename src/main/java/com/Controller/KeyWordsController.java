package com.Controller;

import com.annotation.UserLoginToken;
import com.dao.KeyWordsMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pojo.Article;
import com.pojo.KeyWords;
import com.pojo.paging;
import com.result.Result;
import com.result.ResultFactory;
import com.service.KeyWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @date 2021/4/8 - 18:51
 */
@CrossOrigin
@RestController
public class KeyWordsController {
    @Autowired
    KeyWordsMapper keyWordsMapper;

    @Autowired
    KeyWordsService keyWordsService;

    @GetMapping("/api/admin/keyWords/{pageSize}/{page}")
    public Result listArticles(@PathVariable("page") int page, @PathVariable("pageSize") int size) {
        Page p = PageHelper.startPage(page , size);
        PageInfo<KeyWords> pageInfo = new PageInfo<>(keyWordsService.list(page,size));
        return ResultFactory.buildSuccessResult(new paging((List<KeyWords>) pageInfo.getList(),pageInfo.getTotal()));
    }


    @PostMapping("api/admin/keyWordsAdd")
    public Result add(@RequestBody KeyWords keyWords){
        keyWordsMapper.addKeyWords(keyWords.getContent());
        return ResultFactory.buildSuccessResult("添加成功");
    }

    @DeleteMapping("api/admin/keyWordsDelete/{pid}")
    public Result delete(@PathVariable("pid") Integer pid){
        keyWordsMapper.deleteById(pid);
        return ResultFactory.buildSuccessResult("删除成功");
    }
}
