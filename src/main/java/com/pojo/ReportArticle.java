package com.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @date 2021/4/11 - 16:20
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportArticle implements Serializable {
    private Integer id;
    private Integer uid;
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date articleDate;
}
