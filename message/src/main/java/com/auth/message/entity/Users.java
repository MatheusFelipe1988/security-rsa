package com.auth.message.entity;

import com.auth.message.controller.dto.LoginDTO;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "userid")
    private UUID userid;

    @Column(unique = true)
    private String uname;
    private String password;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_role",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "roleid")
    )
    private Set<Role> roles;

    public UUID getUserid() {
        return userid;
    }

    public void setUserid(UUID userid) {
        this.userid = userid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRole(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isLoginCorrect(LoginDTO loginDTO, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(loginDTO.password(), this.password);
    }
}
