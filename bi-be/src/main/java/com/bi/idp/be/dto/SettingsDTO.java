package com.bi.idp.be.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class SettingsDTO {
    @NotEmpty
    @NotNull
    private String themeName;

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public SettingsDTO() {
    }

    public SettingsDTO(String themeName) {
        this.themeName = themeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SettingsDTO that = (SettingsDTO) o;
        return themeName.equals(that.themeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(themeName);
    }

    @Override
    public String toString() {
        return "SettingsDTO{" +
                "themeName='" + themeName + '\'' +
                '}';
    }
}
