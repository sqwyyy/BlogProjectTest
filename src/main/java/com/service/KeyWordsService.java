package com.service;

import com.dao.KeyWordsMapper;
import com.github.pagehelper.PageHelper;
import com.pojo.KeyWords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @date 2021/4/13 - 20:23
 */
@Service
public class KeyWordsService {

    @Autowired
    KeyWordsMapper keyWordsMapper;

    public List<KeyWords>list(int page,int size){
        PageHelper.startPage(page,size);
        return keyWordsMapper.listForAll();
    }
}
