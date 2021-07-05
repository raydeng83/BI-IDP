package com.bi.idp.be.builder;

import com.bi.idp.be.model.filter.UsersGridFilter;
import com.bi.idp.be.model.SearchCriteria;
import com.bi.idp.be.model.administrator.AdminAccount;
import com.bi.idp.be.model.administrator.AdminAccountSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserSpecificationBuilder {

    private List<SearchCriteria> params;

    public UserSpecificationBuilder() {
        params = new ArrayList<>();
    }

    private UserSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    private void initParams(UsersGridFilter filter) {
        if (filter.getFilterByfirstName() != null) {
            with("firstName", ":", filter.getFilterByfirstName());
        }
        if (filter.getFilterBylastName() != null) {
            with("lastName", ":", filter.getFilterBylastName());
        }
        if (filter.getFilterBylogin() != null) {
            with("login", ":", filter.getFilterBylogin());
        }
        if (filter.getFilterByemail() != null) {
            with("email", ":", filter.getFilterByemail());
        }
        if (filter.getFilterByage() != null) {
            with("age", ":", filter.getFilterByage());
        }
        if (filter.getFilterBystreet() != null) {
            with("street", ":", filter.getFilterBystreet());
        }
        if (filter.getFilterBycity() != null) {
            with("city", ":", filter.getFilterBycity());
        }
        if (filter.getFilterByzipcode() != null) {
            with("zipCode", ":", filter.getFilterByzipcode());
        }
    }

    public Optional<Specification<AdminAccount>> build(UsersGridFilter filter) {
        initParams(filter);

        if (params.size() == 0) {
            return Optional.empty();
        }

        Specification<AdminAccount> result = new AdminAccountSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(new AdminAccountSpecification(params.get(i)));
        }

        return Optional.of(result);
    }
}
