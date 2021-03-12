package com.snow.flowable.domain;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class FileGroup implements Serializable {

    private static final long serialVersionUID = -8173829907939143655L;

    @NotEmpty
    private final String category;

    @NotNull
    private final List<FileEntry> files;
}
