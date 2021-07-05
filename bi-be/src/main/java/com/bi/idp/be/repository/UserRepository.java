package com.bi.idp.be.repository;

import com.bi.idp.be.model.administrator.AdminAccount;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<AdminAccount, Long>, JpaSpecificationExecutor<AdminAccount> {
    Optional<AdminAccount> findByEmail(String email);
}
