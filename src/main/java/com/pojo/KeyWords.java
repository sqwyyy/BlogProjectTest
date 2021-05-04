package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @date 2021/4/4 - 13:28
 */
@Data
@NoArgsConstructor
public class KeyWords {
    private Integer pid;
    private String Content;

    public KeyWords(String content) {
        Content = content;
    }
}
