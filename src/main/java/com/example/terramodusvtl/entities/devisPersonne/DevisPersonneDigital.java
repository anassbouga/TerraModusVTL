package com.example.terramodusvtl.entities.devisPersonne;

import com.example.terramodusvtl.entities.TypeDevis;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity(name = "devis-personne-digital")
public class DevisPersonneDigital implements Serializable {
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
    private DemandeurPersonne demandeurPersonne;

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

    public DemandeurPersonne getDemandeurPersonne() {
        return demandeurPersonne;
    }

    public void setDemandeurPersonne(DemandeurPersonne demandeurPersonne) {
        this.demandeurPersonne = demandeurPersonne;
    }
}
