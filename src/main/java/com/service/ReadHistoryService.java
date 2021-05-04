package com.service;

import com.dao.ArticleMappper;
import com.dao.ReadHistoryMapper;
import com.github.pagehelper.PageHelper;
import com.pojo.ReadHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @date 2021/4/27 - 21:09
 */
@Service
public class ReadHistoryService {
    @Autowired
    ReadHistoryMapper historyMapper;

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleMappper articleMappper;

    @Autowired
    TokenService tokenService;

    public List<ReadHistory>list(int page,int size,HttpServletRequest httpServletRequest){
        PageHelper.startPage(page,size);
        int userId = tokenService.getUser(httpServletRequest).getId();
        return historyMapper.listall(userId);
    }

    public void add(Integer articleId, HttpServletRequest httpServletRequest){
        String userName = articleService.getusername(httpServletRequest).getUsername();
        Integer userId = articleService.getusername(httpServletRequest).getId();
        String articleTitle = articleMappper.getArticlebyId(articleId,userId).getTitle();
        Date date = new Date();
        //获得系统时间. 
        historyMapper.add(new ReadHistory(articleTitle,userId,userName,date));
    }
}
