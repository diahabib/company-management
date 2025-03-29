package com.app.management.companymanagement.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Department {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "description", length = 500)
    private String description;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Employee> employees;

    public Department(long id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }

    public Department() {}

    public Department(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    public Long getId() { if (id == null) {
        return null;} return id; }
    public void setId(long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Employee> getEmployees() { return employees; }
    public void setEmployees(List<Employee> employees) { this.employees = employees; }


    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
