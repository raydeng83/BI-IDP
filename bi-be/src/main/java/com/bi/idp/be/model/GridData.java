package com.bi.idp.be.model;

import java.util.List;

public class GridData<DTO> {
    private long totalCount;
    private List<DTO> items;

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<DTO> getItems() {
        return items;
    }

    public void setItems(List<DTO> items) {
        this.items = items;
    }
}
