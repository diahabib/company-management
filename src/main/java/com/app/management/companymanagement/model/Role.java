package com.app.management.companymanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nom", unique = true, nullable = false)
    private RoleEnum name;

    public enum RoleEnum {
        ADMIN,
        MANAGER,
        EMPLOYEE
    }

    public Role() {}

    public Role(RoleEnum name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RoleEnum getName() { return name; }
    public void setName(RoleEnum name) { this.name = name; }
}

