package com.example.testcrud.dto;

public class UserDetailsDTO {

    private Long id;
    private String additionalInfo;

    public UserDetailsDTO() {
    }

    public UserDetailsDTO(Long id, String additionalInfo) {
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
    public String toString() {
        return "UserDetailsDTO{" +
                "id=" + id +
                ", additionalInfo='" + additionalInfo + '\'' +
                '}';
    }
}
