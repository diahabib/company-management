package com.app.management.companymanagement.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;


@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 75)
    private String nom;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(name = "date_debut",nullable = false)
    private Date dateDebut;

    @Column(name = "date_fin")
    private Date dateFin;

    @Column(name = "statut", nullable = false)
    private String status;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;

    public Project() {}

    public Project(long id, String nom, String description, Date dateDebut, Date dateFin, String status) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.status = status;
    }

    public Project(String nom, String description, Date dateDebut, Date dateFin, String status) {
        this.nom = nom;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.status = status;
    }

    public Long getId() { if (id == null) {
        return null;} return id; }
    public void setId(long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", date_debut=" + dateDebut +
                ", date_fin=" + dateFin +
                ", status='" + status + '\'' +
                '}';
    }
}
