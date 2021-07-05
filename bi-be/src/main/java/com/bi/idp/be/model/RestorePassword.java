package com.bi.idp.be.model;

import com.bi.idp.be.model.administrator.AdminAccount;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class RestorePassword {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = AdminAccount.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private AdminAccount user;

    @Column(nullable = false)
    private LocalDateTime expiresIn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AdminAccount getUser() {
        return user;
    }

    public void setUser(AdminAccount user) {
        this.user = user;
    }

    public LocalDateTime getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(LocalDateTime expiresIn) {
        this.expiresIn = expiresIn;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresIn);
    }
}
