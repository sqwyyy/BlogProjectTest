package com.dao;

import com.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @date 2020/2/18 - 13:23
 */


@Mapper
public interface ArticleMappper {
    public Article getArticlebyId(@Param("id") int id,@Param("uid")int uid);

    public Article getArticlebytitle(Article article);

    public void Addarticle(Article article);

    public void UpdateArticle(Article article);

    public void DeletebyID(@Param("id")int id,@Param("uid")int uid);

    public List<Article> getall(@Param("uid")int uid);

    public List<Article>all();

    public List<Article> getbyabstract(Article article);

}
