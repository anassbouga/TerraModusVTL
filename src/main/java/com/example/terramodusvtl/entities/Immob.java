package com.example.terramodusvtl.entities;

import jakarta.persistence.*;

@Entity(name = "immob")
public class Immob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "libelle")
    private String libelle;

    @Column(name = "superficie")
    private double superficie;
    @Column(name = "n-titre-foncier")
    private String nTitreFoncier;
    private TypeBien typeBien;
    private int qantite;

    @Column(name = "ville")
    private String ville;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "nombre-actifs")
    private String nActifs;


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

    public TypeBien getTypeBien() {
        return typeBien;
    }

    public void setTypeBien(TypeBien typeBien) {
        this.typeBien = typeBien;
    }

    public int getQantite() {
        return qantite;
    }

    public void setQantite(int qantite) {
        this.qantite = qantite;
    }

    public String getnTitreFoncier() {
        return nTitreFoncier;
    }

    public void setnTitreFoncier(String nTitreFoncier) {
        this.nTitreFoncier = nTitreFoncier;
    }

    public double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getnActifs() {
        return nActifs;
    }

    public void setnActifs(String nActifs) {
        this.nActifs = nActifs;
    }
}
