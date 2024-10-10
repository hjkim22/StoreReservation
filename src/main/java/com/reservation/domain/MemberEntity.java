package com.reservation.domain;

import com.reservation.type.MemberType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(nullable = false)
    private String username;

    @NotNull
    @Size(min = 6)
    @Column(nullable = false)
    private String password;

    @NotNull
    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // MemberType 에 따라 다르게 반환
        return List.of(new SimpleGrantedAuthority("ROLE_" + memberType.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired(); // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked(); // 계정 잠김 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired(); // 자격 증명 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled(); // 계정 활성화 여부
    }
}