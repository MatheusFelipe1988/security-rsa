package com.auth.message.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleid")
    private Long roleid;

    private String name;

    public Long getRoleid() {
        return roleid;
    }

    public void setRoleid(Long roleid) {
        this.roleid = roleid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum Values{
        ADMIN(1L),
        BASIC(2L);

        long roleid;

        Values(long roleid) {
            this.roleid = roleid;
        }

        public long getRoleid() {
            return roleid;
        }
    }

}
