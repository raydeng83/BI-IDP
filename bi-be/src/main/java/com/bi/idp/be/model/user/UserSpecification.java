package com.bi.idp.be.model.user;

import com.bi.idp.be.exception.auth.BadRequestHttpException;
import com.bi.idp.be.model.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserSpecification implements Specification<User> {
    public static final String AGE = "age";

    private static final long serialVersionUID = -4415234963138321694L;

    private transient SearchCriteria criteria;

    public UserSpecification(SearchCriteria searchCriteria) {
        this.criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (!criteria.getOperation().equalsIgnoreCase(":")) {
            return null;
        }
        if (criteria.getKey().equals(AGE)) {
            String ageValue = (String) criteria.getValue();
            try {
                return builder.equal(root.get(AGE), Integer.valueOf(ageValue));
            } catch (NumberFormatException ex) {
                throw new BadRequestHttpException();
            }
        } else {
            return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        }
    }
}
