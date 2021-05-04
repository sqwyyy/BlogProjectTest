package com.service;

import com.dao.ReportArticleMapper;
import com.dao.ReportMapper;
import com.github.pagehelper.PageHelper;
import com.pojo.Article;
import com.pojo.Report;
import com.pojo.ReportArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @date 2021/4/9 - 20:49
 */
@Service
@CacheConfig(cacheNames = "report")
public class ReportService {

    @Autowired
    ReportMapper reportMapper;

    @Autowired
    ArticleService articleService;

    @Autowired
    ReportArticleMapper reportArticleMapper;

    @Autowired
    TokenService tokenService;


    public Report findReportByContent(String content){
        return reportMapper.findbyContent(content);
    }

    public Integer findReportNum(int Aid){
        return reportMapper.findNum(Aid);
    }

    public void deleteReport(String content){
        reportMapper.deleteByContent(content);
    }

    public void addReport(String content,Integer Aid,HttpServletRequest httpServletRequest){
        if(findReportNum(Aid) >= 3){
            int uid = tokenService.getUser(httpServletRequest).getId();
            Article article = articleService.getbyId(Aid,httpServletRequest);
            reportArticleMapper.add(new ReportArticle(Aid,uid,article.getTitle(),content,article.getArticleDate()));
        }
        reportMapper.addReport(new Report(content,new Date(),Aid));
    }

    @Cacheable
    public List<ReportArticle> listForAll(int page, int size){
        PageHelper.startPage(page,size);
        return reportArticleMapper.getall();
    }



   // public List<Article>ReportArticle()
}
