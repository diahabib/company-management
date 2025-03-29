package com.app.management.companymanagement.model;

import jakarta.persistence.*;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String description;

    @Column(name = "statut", nullable = false)
    private String statut;

    @ManyToOne
    @JoinColumn(name = "employe_id",nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "projet_id" ,nullable = false)
    private Project project;


    public Task() {}

    public Task(long id, String description, String statut) {
        this.id = id;
        this.description = description;
        this.statut = statut;
    }

    public Task(String description, Long employe_id, Long projet_id, String status) {
        this.description = description;
        this.statut = status;
    }

    public Long getId() { if (id == null) { return null; } return id; }
    public void setId(long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", statut='" + statut + '\'' +
                ", employe=" + employee +
                ", projet=" + project +
                '}';
    }
}
