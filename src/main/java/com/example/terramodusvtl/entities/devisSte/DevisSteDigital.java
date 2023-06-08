package com.example.terramodusvtl.entities.devisSte;

import com.example.terramodusvtl.entities.TypeDevis;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity(name = "devis-ste-digital")
public class DevisSteDigital implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 3)
    private Long id;

    @Column(name = "besoin",columnDefinition = "TEXT")
    private String besoin;

    @Column(name = "created-at")
    @JsonFormat(pattern="dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date createdAt = new Date();
    @ManyToOne
    private TypeDevis typeDevis;

    @ManyToOne
    private DemandeurSte demandeurSte;

    private String siege;
    
    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    public String getBesoin() {
        return besoin;
    }

    public void setBesoin(String besoin) {
        this.besoin = besoin;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public TypeDevis getTypeDevis() {
        return typeDevis;
    }

    public void setTypeDevis(TypeDevis typeDevis) {
        this.typeDevis = typeDevis;
    }

    public DemandeurSte getDemandeurSte() {
        return demandeurSte;
    }

    public void setDemandeurSte(DemandeurSte demandeurSte) {
        this.demandeurSte = demandeurSte;
    }

    public String getSiege() {
        return siege;
    }

    public void setSiege(String siege) {
        this.siege = siege;
    }
}
