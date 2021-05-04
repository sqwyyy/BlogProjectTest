package com.dao;

import com.pojo.ReadHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @date 2021/4/27 - 21:08
 */
@Mapper
public interface ReadHistoryMapper {
    public List<ReadHistory>listall(Integer userId);

    public void add(ReadHistory history);

    public void deleteById(Integer id);

    public  ReadHistory lastOne(Integer id);
}
