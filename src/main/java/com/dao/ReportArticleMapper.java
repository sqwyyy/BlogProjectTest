package com.dao;

import com.pojo.Article;
import com.pojo.ReportArticle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @date 2021/4/11 - 16:28
 */
@Mapper
public interface ReportArticleMapper {
    public List<ReportArticle> getall();

    public void add(ReportArticle reportArticle);

    public void delete(@Param("id")int id);

}
