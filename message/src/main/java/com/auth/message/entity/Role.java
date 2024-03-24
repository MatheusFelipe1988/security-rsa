package com.auth.message.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleid")
    private Long roleid;

    private String name;

    public enum Values{
        ADMIN(1L),
        BASIC(2L);

        long roleid;

        Values(long roleid) {
            this.roleid = roleid;
        }
    }

}
