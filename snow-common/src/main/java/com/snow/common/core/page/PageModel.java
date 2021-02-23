package com.snow.common.core.page;

import java.io.Serializable;
import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/26 14:33
 */
public class PageModel<T> implements Serializable {
    private static final long serialVersionUID = -1183925952330439225L;
    public static final int DEFAULT_PAGE_SIZE = 10;
    private int pageNo = 1;
    private int pageSize = 10;
    private int totalCount;
    private List<T> pagedRecords;

    public PageModel() {
    }

    public int getTotalPages() {
        return (this.totalCount + this.pageSize - 1) / this.pageSize;
    }

    public int getPageFrom() {
        return (this.pageNo - 1) * this.pageSize + 1;
    }

    public int getPageEnd() {
        int endIndex = this.pageNo * this.pageSize;
        return endIndex > this.totalCount ? this.totalCount : endIndex;
    }

    public int getOffset() {
        return (this.pageNo - 1) * this.pageSize;
    }

    public int getPrevPage() {
        return this.pageNo <= 1 ? 1 : this.pageNo - 1;
    }

    public int getNextPage() {
        if (this.pageNo > this.getTotalPages()) {
            return this.getTotalPages() == 0 ? 1 : this.getTotalPages();
        } else {
            return this.pageNo + 1;
        }
    }

    public boolean isFirstPage() {
        return this.pageNo == 1;
    }

    public boolean isLastPage() {
        return this.pageNo == this.getTotalPages();
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getPagedRecords() {
        return this.pagedRecords;
    }

    public void setPagedRecords(List<T> pagedRecords) {
        this.pagedRecords = pagedRecords;
    }

    public String toString() {
        return "PageModel [pageNo=" + this.pageNo + ", pageSize=" + this.pageSize + ", totalCount=" + this.totalCount + ", pagedRecords=" + this.pagedRecords + "]";
    }
}
