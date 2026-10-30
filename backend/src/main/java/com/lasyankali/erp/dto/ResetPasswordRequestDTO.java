package com.lasyankali.erp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequestDTO {
	@NotBlank(message = "Reset token is required")
    @Size(max = 256, message = "Reset token is invalid")
    private String token;

    @NotBlank(message = "New password is required")
    @Size(
        min = 12,
        max = 72,
        message = "Password must contain between 12 and 72 characters"
    )
    private String newPassword;

    public ResetPasswordRequestDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
