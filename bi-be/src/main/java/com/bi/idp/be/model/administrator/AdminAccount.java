package com.bi.idp.be.model.administrator;

import com.bi.idp.be.model.Image;
import com.bi.idp.be.model.Settings;
import com.bi.idp.be.model.role.Role;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
public class AdminAccount implements Serializable {

    private static final long serialVersionUID = -4214325494311301431L;

    public AdminAccount() { }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "login", nullable = false)
    @NotEmpty(message = "Please, provide a login")
    private String login;

    @Column(name = "email", nullable = false)
    @NotEmpty(message = "Please, provide an email")
    private String email;

    @Column(name = "age")
    private Integer age;

    @Column(name = "password_hash", nullable = false)
    @NotEmpty(message = "Please, provide a password")
    private String passwordHash;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "administrator_roles",
            joinColumns = {@JoinColumn(name = "administrator_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "settings_id", referencedColumnName = "id")
    private Settings settings;

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    @Column(name = "address_street")
    private String street;

    @Column(name = "address_city")
    private String city;

    @Column(name = "address_zip_code")
    private String zipCode;

    @Column(name = "address_lat")
    private Double lat;

    @Column(name = "address_lng")
    private Double lng;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminAccount admin = (AdminAccount) o;
        return isDeleted == admin.isDeleted &&
                Objects.equals(id, admin.id) &&
                Objects.equals(firstName, admin.firstName) &&
                Objects.equals(lastName, admin.lastName) &&
                Objects.equals(login, admin.login) &&
                Objects.equals(email, admin.email) &&
                Objects.equals(age, admin.age) &&
                Objects.equals(passwordHash, admin.passwordHash) &&
                Objects.equals(roles, admin.roles) &&
                Objects.equals(settings, admin.settings) &&
                Objects.equals(street, admin.street) &&
                Objects.equals(city, admin.city) &&
                Objects.equals(zipCode, admin.zipCode) &&
                Objects.equals(lat, admin.lat) &&
                Objects.equals(lng, admin.lng) &&
                Objects.equals(createdAt, admin.createdAt) &&
                Objects.equals(updatedAt, admin.updatedAt) &&
                Objects.equals(image, admin.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, login, email, age, passwordHash, isDeleted,
                roles, settings, street, city, zipCode, lat, lng, createdAt, updatedAt, image);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", passwordHash='" + passwordHash + '\'' +
                ", isDeleted=" + isDeleted +
                ", roles=" + roles +
                ", settings=" + settings +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", image=" + image +
                '}';
    }
}
