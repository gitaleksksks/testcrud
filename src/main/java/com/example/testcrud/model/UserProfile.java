package com.example.testcrud.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "user_details")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "additional_info", columnDefinition = "TEXT")
    private String additionalInfo;

    public UserProfile() {
    }

    public UserProfile(Long id, String additionalInfo) {
        this.id = id;
        this.additionalInfo = additionalInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(additionalInfo, that.additionalInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, additionalInfo);
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "id=" + id +
                ", additionalInfo='" + additionalInfo + '\'' +
                '}';
    }
}
