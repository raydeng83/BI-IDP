package com.bi.idp.be.builder;

import com.bi.idp.be.filter.BaseFilter;
import com.bi.idp.be.model.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageableBuilder {
    public static final String SUM = "sum";
    public static final String VALUE = "value";
    public static final String ZIPCODE_LOW_CASE = "zipcode";
    public static final String ZIPCODE_CAMEL_CASE = "zipCode";

    public Pageable build(BaseFilter filter) {
        int pageNumber = filter.getPageNumber() - 1;
        int pageSize = filter.getPageSize();
        //in dto at front this field calls sum
        String sortBy = filter.getSortBy();
        String sortField = sortBy != null && sortBy.equals(SUM) ? VALUE : sortBy;

        // if we want sort by zipcode, it must be in camel case
        sortField = sortField != null && sortField.equals(ZIPCODE_LOW_CASE) ? ZIPCODE_CAMEL_CASE : sortField;

        Pageable pageable;
        SortOrder sortOrder = filter.getOrderBy() == null ? SortOrder.EMPTY
                : SortOrder.valueOfIgnoreCase(filter.getOrderBy());
        switch (sortOrder) {
            case ASC:
                pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortField));
                break;
            case DESC:
                pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortField).descending());
                break;
            case EMPTY:
                pageable = PageRequest.of(pageNumber, pageSize, Sort.by("Id").descending());
                break;
            default:
                throw new IllegalArgumentException("Wrong sort order");
        }
        return pageable;
    }
}
