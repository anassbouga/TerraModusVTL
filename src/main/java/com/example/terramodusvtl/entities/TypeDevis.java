package com.example.terramodusvtl.entities;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "type-devis")
public class TypeDevis implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "responsable")
    private String responsable;


    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
