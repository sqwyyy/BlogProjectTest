package com.dao;

import com.pojo.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @date 2021/4/9 - 20:15
 */
@Mapper
public interface ReportMapper {
    public List<Report> getAllReport();

    public void addReport(Report report);

    public void deleteByContent(String content);

    public Report findbyContent(String content);

    public Integer findNum(@Param("Aid") int Aid);
}
