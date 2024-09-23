package com.example.SWP.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Setter
@Getter

@Entity
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    boolean isDeleted = false;

    @NotBlank(message = "This username can not be empty!")
    @Column(unique = true)
    String username;

    @NotBlank(message = "This full name can not be empty!")
    String Fullname;


    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Phone invalid!")
    @Column(unique = true)
    String Phone_number;

    @Email(message = "Email not valid!")
    @Column(unique = true)
    String email;

    Date create_date;

    String status;

    @NotBlank(message = "Password can not blank!")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password;

    long Roleid;

    String city;

    String state;

    String country;

    String specific_Address;

    @OneToOne
    ForgotPassword forgotPassword;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.username;
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
