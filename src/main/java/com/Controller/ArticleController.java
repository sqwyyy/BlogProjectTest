package com.Controller;

import com.annotation.UserLoginToken;
import com.dao.ReadHistoryMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pojo.Article;
import com.pojo.ReadHistory;
import com.pojo.paging;
import com.result.Result;
import com.result.ResultFactory;
import com.service.ArticleService;
import com.service.ReadHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @date 2020/2/18 - 14:35
 */


@CrossOrigin
@RestController
@Api(tags = "文章管理相关接口")
public class ArticleController {

    @Autowired
    ArticleService articleService;


    @ApiOperation("修改或者添加文章")
    @UserLoginToken
    @PostMapping("api/admin/content/article")
    public Result add(@RequestBody Article article,HttpServletRequest httpServletRequest){
        if(articleService.checkSensitiveWord(article) == true){
            return ResultFactory.buildSuccessResult("存在敏感词汇");
        }
        articleService.AddOrUpdate(article,httpServletRequest);
        return ResultFactory.buildSuccessResult("保存成功");
    }


    @ApiOperation("根据页码查询该页的文章")
    @UserLoginToken
    @GetMapping("/api/article/{pageSize}/{page}")
    public Result listArticles(@PathVariable("page") int page, @PathVariable("pageSize") int size,HttpServletRequest httpServletRequest) {
        Page p = PageHelper.startPage(page , size);
        PageInfo<Article>pageInfo = new PageInfo<>(articleService.list(page,size,httpServletRequest));
        return ResultFactory.buildSuccessResult(new paging((List<Article>) pageInfo.getList(),pageInfo.getTotal()));
    }


    @ApiOperation("根据文章id删除文章")
    @UserLoginToken
    @DeleteMapping("/api/admin/content/article/{id}")
    public Result deleteArticle(@PathVariable("id") int id,HttpServletRequest httpServletRequest) {
        articleService.delete(id,httpServletRequest);
        return ResultFactory.buildSuccessResult("删除成功");
    }

    @ApiOperation("通过id获取文章")
    @GetMapping("/api/article/{id}")
    public Result getarticlebyid(@PathVariable("id")int id,HttpServletRequest httpServletRequest){
        return ResultFactory.buildSuccessResult(articleService.getbyId(id,httpServletRequest));
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/api/article/userinformation/Abstract")
    public Result getuserinformation(HttpServletRequest httpServletRequest){
        return ResultFactory.buildSuccessResult(articleService.getcover(httpServletRequest));
    }

    @ApiOperation("获取用户名字")
    @GetMapping("/api/article/userinformation/username")
    public Result getusername(HttpServletRequest httpServletRequest){
        return ResultFactory.buildSuccessResult(articleService.getusername(httpServletRequest));
    }

    @ApiOperation("获取文章类别的数目")
    @GetMapping("/api/article/Abstract/num")
    public Result getcovernum(HttpServletRequest httpServletRequest){
        return ResultFactory.buildSuccessResult(articleService.getcovernum(httpServletRequest));
    }

    @ApiOperation("根据页码查询该页的文章分类")
    @GetMapping("/api/article/Abstract/{Abstractencode}/{size}/{page}")
    public Result ListAbstract(@PathVariable("Abstractencode") String Abstractencode,@PathVariable("size") int size,@PathVariable("page") int page,HttpServletRequest httpServletRequest){
        String Abstract = "";
        ReentrantLock r = new ReentrantLock();
        byte [] data = Base64.getDecoder().decode(Abstractencode);
        try {
            Abstract = new String(data,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Page p = PageHelper.startPage(page,size);
        PageInfo<Article>pageInfo = new PageInfo<>(articleService.listbyabstract(page,size,httpServletRequest,Abstract));
        return ResultFactory.buildSuccessResult(new paging((List<Article>) pageInfo.getList(),pageInfo.getTotal()));
    }


    @ApiOperation("文章推荐")
    @GetMapping("/api/article/Abstract/Recommend")
    public Result recommendArticles(HttpServletRequest httpServletRequest) throws IOException {
        return ResultFactory.buildSuccessResult(articleService.recommend(httpServletRequest));

    }

}
