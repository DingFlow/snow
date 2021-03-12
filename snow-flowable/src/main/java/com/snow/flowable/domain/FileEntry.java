package com.snow.flowable.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileEntry implements Serializable {


    private static final long serialVersionUID = 7310292292228252227L;

    /**
     * 文件key
     */
    private String key;

    /**
     * 文件name
     */
    private String name;

    /**
     * 文件url
     */
    private String url;

}
