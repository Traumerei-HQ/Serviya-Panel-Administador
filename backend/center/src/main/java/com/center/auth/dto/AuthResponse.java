package com.center.auth.dto;

import com.center.auth.model.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String name;
    private String email;
    private UserRole role;
}
