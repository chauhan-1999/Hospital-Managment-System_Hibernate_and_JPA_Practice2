package com.chauhan.hospitalManagementSystem.entity;

import com.chauhan.hospitalManagementSystem.enums.AuthProviderType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users", indexes = {
        @Index(name = "idx_provider_id_provider_type", columnList = "providerId, providerType")
})
public class User implements UserDetails {  //security

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(unique = true, nullable = false)
    private String username;//can behave like emailId or anyUniqueId
    private String password;

    private String providerId;

    @Enumerated(EnumType.STRING)
    private AuthProviderType providerType;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}