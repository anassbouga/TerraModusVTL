package com.example.terramodusvtl.entities.devisSte;

import jakarta.persistence.*;

@Entity(name = "demandeur-ste")
public class DemandeurSte{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 3)
    private Long id;
    @Column(name = "nom-ste")
    private String nomSte;
    @Column(name = "ice")
    private String ice;
    @Column(name = "nom-responsable")
    private String nomResponsable;
    @Column(name = "email")
    private String email;
    @Column(name = "phone",length = 15)
    private int phone;
    @Column(name = "siege")
    private String siege;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }


    public String getNomResponsable() {
        return nomResponsable;
    }

    public void setNomResponsable(String nomResponsable) {
        this.nomResponsable = nomResponsable;
    }

    public String getNomSte() {
        return nomSte;
    }

    public void setNomSte(String nomSte) {
        this.nomSte = nomSte;
    }

    public String getIce() {
        return ice;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiege() {
        return siege;
    }

    public void setSiege(String siege) {
        this.siege = siege;
    }
}
