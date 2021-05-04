package com.dao;

import com.pojo.KeyWords;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @date 2021/4/5 - 19:11
 */
@Mapper
public interface KeyWordsMapper {
    public List<KeyWords>listForAll();

    public void addKeyWords(String Content);

    public void deleteById(Integer id);
}
