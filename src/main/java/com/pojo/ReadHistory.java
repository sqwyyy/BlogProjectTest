package com.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @date 2021/4/27 - 21:05
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReadHistory {
    private Integer id;
    private String articleName;
    private Integer userId;
    private String userName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date readTime;

    public ReadHistory(String articleName,Integer userId,String userName,Date readTime){
        this.articleName = articleName;
        this.userId = userId;
        this.userName = userName;
        this.readTime = readTime;
    }

}
