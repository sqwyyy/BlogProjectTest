package com.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @date 2020/2/18 - 12:54
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    @ApiModelProperty(value = "用户id")
    private Integer id;
    private Integer uid;
    private String title;
    private String contentHtml;
    private String contentMd;
    private String Abstract;
    private String cover;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date articleDate;

    public Article(Integer uid, String title){
        this.uid = uid;
        this.title = title;
    }

}
