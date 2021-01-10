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

    private String key;


    private String name;

    private String url;

}
