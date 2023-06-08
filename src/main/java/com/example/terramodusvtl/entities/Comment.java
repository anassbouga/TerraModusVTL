package com.example.terramodusvtl.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;


@Entity(name = "commentaire")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "nom")
    private String nom;

    @Column(name = "email")
    private String email;

    @Column(name = "site-web")
    private String webSite;
    @Column(name = "text",columnDefinition = "TEXT")
    private String text;
    @Column(name = "date-creation")
    private Date createdAt;
    @Column(name = "date-modification")
    private Date modifiedAt;

    @ManyToOne
    private Article article;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
