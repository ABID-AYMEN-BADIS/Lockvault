package com.securevault.dto;

import jakarta.validation.constraints.NotBlank;

public class PasswordEntryDTO {
    
    @NotBlank
    private String platform;
    
    @NotBlank
    private String username;
    
    @NotBlank
    private String password;

    public PasswordEntryDTO() {
    }
    public PasswordEntryDTO (String platform, String username, String password) {
        this.platform = platform;
        this.username = username;
        this.password = password;
    }

    // Getters & Setters

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
