package com.service;

import com.config.SensitiveWordInit;
import com.dao.ArticleMappper;
import com.dao.KeyWordsMapper;
import com.dao.ReadHistoryMapper;
import com.github.pagehelper.PageHelper;
import com.pojo.*;
import com.untils.AnsjUntils;
import com.untils.SensitivewordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.Kernel;
import java.io.IOException;
import java.util.*;

/**
 * @date 2020/2/18 - 14:31
 */
@Service
public class ArticleService {
    @Autowired
    ArticleMappper articleMappper;

    @Autowired
    TokenService tokenService;

    @Autowired
    SensitiveWordInit sensitiveWordInit;

    @Autowired
    SensitivewordUtils sensitivewordUtils;

    @Autowired
    KeyWordsMapper keyWordsMapper;

    public void AddOrUpdate(Article article, HttpServletRequest httpServletRequest){
        int uid = tokenService.getUser(httpServletRequest).getId();
        article.setUid(uid);
        Article articlebytitle = articleMappper.getArticlebytitle(article);
        if(!checkSensitiveWord(article))
        {
            Date date = new Date();//获得系统时间. 
            article.setArticleDate(date);
            if(articlebytitle != null){
                article.setUid(uid);
                articleMappper.UpdateArticle(article);
            }
            else{
                article.setUid(uid);
                articleMappper.Addarticle(article);
            }
        }
    }


    public boolean checkSensitiveWord(Article article){
        List<KeyWords>keyWords = keyWordsMapper.listForAll();
        SensitiveWordInit sensitiveWordInit = new SensitiveWordInit();
        Map sensitiveWordMap   = sensitiveWordInit.initKeyWord(keyWords);
        SensitivewordUtils.sensitiveWordMap = sensitiveWordMap;
        boolean ans = SensitivewordUtils.isContaintSensitiveWord(article.getContentMd(),2);
        return ans;
    }

    public List<Article> list(int page,int size,HttpServletRequest httpServletRequest){
        int uid = tokenService.getUser(httpServletRequest).getId();
        PageHelper.startPage(page,size);
        return articleMappper.getall(uid);
    }

    public List<Article> listbyabstract(int page,int size,HttpServletRequest httpServletRequest,String abstrac){
        int uid = tokenService.getUser(httpServletRequest).getId();
        PageHelper.startPage(page,size);
        Article article = new Article();
        article.setUid(uid);
        article.setAbstract(abstrac);
        return articleMappper.getbyabstract(article);
    }

    public void  delete(int id,HttpServletRequest httpServletRequest){
        int uid = tokenService.getUser(httpServletRequest).getId();
        articleMappper.DeletebyID(id,uid);
    }

    public Article getbyId(int id,HttpServletRequest httpServletRequest){
        int uid = tokenService.getUser(httpServletRequest).getId();
        return articleMappper.getArticlebyId(id,uid);
    }

    public HashMap<String,Integer> getcoverandnum(HttpServletRequest httpServletRequest){
        int uid = tokenService.getUser(httpServletRequest).getId();
        List<Article> articles = articleMappper.getall(uid);
        ListIterator<Article>listIterator = articles.listIterator();
        HashMap<String,Integer>vis = new HashMap<>();
        while(listIterator.hasNext()){
            String abstrac = listIterator.next().getAbstract();
            if(vis.containsKey(abstrac)){
                vis.put(abstrac,vis.get(abstrac)+1);
            }
            else vis.put(abstrac,1);
        }
        return vis;
    }

    public ArrayList<String> getcover(HttpServletRequest httpServletRequest){
        HashMap<String,Integer>vis = getcoverandnum(httpServletRequest);
        Iterator iterator = vis.entrySet().iterator();
        ArrayList<String>abstracs = new ArrayList<>();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            if(entry.getKey()==null) continue;
            abstracs.add((String) entry.getKey());
        }
        return abstracs;
    }

    public ArrayList<Integer> getcovernum(HttpServletRequest httpServletRequest){
        HashMap<String,Integer>vis = getcoverandnum(httpServletRequest);
        Iterator iterator = vis.entrySet().iterator();
        ArrayList<Integer>nums = new ArrayList<>();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            if(entry.getKey()==null) continue;
            nums.add((Integer)entry.getValue());
        }
        return nums;
    }

    public User getusername(HttpServletRequest httpServletRequest){
        return tokenService.getUser(httpServletRequest);
    }


    @Autowired
    ReadHistoryMapper historyMapper;

    public List<Article>recommend(HttpServletRequest httpServletRequest) throws IOException {

        ReadHistory lastOne = historyMapper.lastOne(tokenService.getUser(httpServletRequest).getId());
        Article articleOne = new Article();
        if(lastOne == null){
            articleOne = articleMappper.getArticlebyId(26,1);
        }
        else{
            articleOne = articleMappper.getArticlebytitle(new Article(lastOne.getUserId(),lastOne.getArticleName()));
        }

        List<Article>articles = articleMappper.all();
        List<Article>articleList = new LinkedList<>();
        AnsjUntils ansj = new AnsjUntils();
        for(Article article : articles){
            if(ansj.checkSameKeyWord(articleOne,article) == true){
                articleList.add(article);
            }
        }
        List<ArtcileKeyWords> keyWords = new LinkedList<>();
        for(Article article : articleList){
            keyWords.add(new ArtcileKeyWords(article,ansj.isLike(articleOne,article)));
        }
        Collections.sort(keyWords, new Comparator<ArtcileKeyWords>() {
            @Override
            public int compare(ArtcileKeyWords u1, ArtcileKeyWords u2) {
                if(u1.getCos() > u2.getCos()) {
                    //return -1:即为正序排序
                    return -1;
                } else {
                    //return 1: 即为倒序排序
                    return 1;
                }
            }
        });
        articles.clear();
        for(ArtcileKeyWords keyWords1 : keyWords){
            articles.add(keyWords1.getArticle());
            if(articles.size() >= 5){
                break;
            }
        }
        return articles;
    }

}
