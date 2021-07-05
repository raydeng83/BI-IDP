package com.bi.idp.be.model;

public enum SortOrder {
    ASC,
    DESC,
    EMPTY;

    public static SortOrder valueOfIgnoreCase(String sortOrder) {
        sortOrder = sortOrder.toUpperCase();
        return valueOf(sortOrder);
    }
}
