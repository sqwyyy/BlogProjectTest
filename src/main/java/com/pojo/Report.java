package com.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @date 2021/4/9 - 20:12
 */
@Data
@NoArgsConstructor
public class Report implements Serializable {
    private Integer id;
    private String content;
    private Integer Aid;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date articleDate;

    public Report(String content, Date articleDate, Integer Aid) {
        this.content = content;
        this.articleDate = articleDate;
        this.Aid = Aid;
    }
}
