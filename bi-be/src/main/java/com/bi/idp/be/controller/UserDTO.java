package com.bi.idp.be.controller;

import com.bi.idp.be.model.Settings;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

public class UserDTO {

    private Long id;

    @NotEmpty
    @NotNull
    private String login;

    @NotEmpty
    @NotNull
    private String email;

    private String firstName;
    private String lastName;
    private Integer age;
    private Set<String> roles;
    private Settings settings;
    private String imageBase64;

    public UserDTO() { }

    public UserDTO(String login, String email) {
        this.login = login;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) &&
                Objects.equals(login, userDTO.login) &&
                Objects.equals(email, userDTO.email) &&
                Objects.equals(firstName, userDTO.firstName) &&
                Objects.equals(lastName, userDTO.lastName) &&
                Objects.equals(age, userDTO.age) &&
                Objects.equals(roles, userDTO.roles) &&
                Objects.equals(settings, userDTO.settings) &&
                Objects.equals(imageBase64, userDTO.imageBase64);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, email, firstName, lastName, age, roles, settings, imageBase64);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", roles=" + roles +
                ", settings=" + settings +
                ", imageBase64='" + imageBase64 + '\'' +
                '}';
    }
}

