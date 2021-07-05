package com.bi.idp.be.model;


import com.bi.idp.be.model.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "settings")
public class Settings implements Serializable {
    private static final long serialVersionUID = 2168089762510982363L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "theme_name", nullable = false)
    private String themeName;

    @OneToOne(mappedBy = "settings")
    private User user;

    public Settings(String themeName) {
        this.themeName = themeName;
    }

    public Settings() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return Objects.equals(id, settings.id) &&
                Objects.equals(themeName, settings.themeName) &&
                Objects.equals(user, settings.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, themeName, user);
    }

    @Override
    public String toString() {
        return "Settings{" +
                "id=" + id +
                ", themeName='" + themeName + '\'' +
                ", user=" + user +
                '}';
    }
}
