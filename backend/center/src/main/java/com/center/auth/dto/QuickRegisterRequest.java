package com.center.auth.dto;

import lombok.Data;

/**
 * DTO para el registro rápido de usuario
 */
@Data
public class QuickRegisterRequest {
    private String email;
    private String password;
    private boolean acceptedHabeasData;
}
