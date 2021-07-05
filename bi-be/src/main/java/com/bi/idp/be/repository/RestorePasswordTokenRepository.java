package com.bi.idp.be.repository;

import com.bi.idp.be.model.RestorePassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Repository
public interface RestorePasswordTokenRepository extends JpaRepository<RestorePassword, Long> {

    RestorePassword findByToken(String token);

    @Transactional
    @Modifying
    @Query("delete from RestorePassword rp where rp.expiresIn < :localDateTime")
    void deleteExpiredRestorePasswordTokens(LocalDateTime localDateTime);
}
