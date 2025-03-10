package com.center.auth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Representa al usuario en la base de datos.
 * Tabla: users
 */
@Entity
@Table(name = "users") // Evita conflicto con 'user' (palabra reservada)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campos obligatorios para registro rápido
    @Column(unique = true, nullable = false)
    @NotBlank
    @Email
    private String email;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    /**
     * Indica si el usuario ha aceptado la política de habeas data.
     */
    private boolean acceptedHabeasData;

    // Campo para el nombre, que se completará luego.
    private String name;

    // Campos opcionales (post-creación)
    @Enumerated(EnumType.STRING)
    private UserRole role;  // Ej: USER, ADMIN

    /**
     * Tipo de identificación (cc, id, nit, etc.)
     */
    private String docType;

    /**
     * Número de identificación.
     * Ojo: si puede llevar ceros a la izquierda, mejor usar String en lugar de int.
     */
    private Integer docNumber;

    /**
     * Teléfono del usuario.
     */
    private String phone;

    // ================================
    // Implementación de UserDetails
    // ================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Si implementas roles, mapéalos aquí.
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
