package com.example.terramodusvtl.entities.devisPersonne;

import com.example.terramodusvtl.entities.Immob;
import com.example.terramodusvtl.entities.TypeDevis;
import com.example.terramodusvtl.entities.TypeValeurEv;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "devis-personne-amiable")
public class DevisPersonneAmiable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 3)
    private Long id;
    @Column(name = "type-valeur-éval")
    private TypeValeurEv typeValeurEv;

    @OneToMany
    private List<Immob>  immobs;
    @Column(name = "n-titre-foncier")
    private String nTitreFoncier;

    @Column(name = "latitude")
    private Double Lat;

    @Column(name = "longitude")
    private Double Lon;
    @Column(name = "indemnite-kilometrique")
    private Double IK;
    @ManyToOne
    private DemandeurPersonne demandeurPersonne;

    @Column(name = "besoin",columnDefinition = "TEXT")
    private String besoin;

    @Column(name = "created-at")
    @JsonFormat(pattern="dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date createdAt = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_devis_id")
    private TypeDevis typeDevis;
    private String livrables;
    @Column(name = "docs-consultaion-admin")
    private String consDocs;
    @Column(name = "docs-consultaion-admin-supp")
    private String consDocsSupp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public TypeValeurEv getTypeValeurEv() {
        return typeValeurEv;
    }

    public void setTypeValeurEv(TypeValeurEv typeValeurEv) {
        this.typeValeurEv = typeValeurEv;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLon() {
        return Lon;
    }

    public void setLon(Double lon) {
        Lon = lon;
    }

    public Double getIK() {
        return IK;
    }

    public void setIK(Double IK) {
        this.IK = IK;
    }

    public String getnTitreFoncier() {
        return nTitreFoncier;
    }

    public void setnTitreFoncier(String nTitreFoncier) {
        this.nTitreFoncier = nTitreFoncier;
    }

    public DemandeurPersonne getDemandeurPersonne() {
        return demandeurPersonne;
    }

    public void setDemandeurPersonne(DemandeurPersonne demandeurPersonne) {
        this.demandeurPersonne = demandeurPersonne;
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

    public String getLivrables() {
        return livrables;
    }

    public void setLivrables(String livrables) {
        this.livrables = livrables;
    }


    public List<Immob> getImmobs() {
        return immobs;
    }

    public void setImmobs(List<Immob> immobs) {
        this.immobs = immobs;
    }

    public String getConsDocs() {
        return consDocs;
    }

    public void setConsDocs(String consDocs) {
        this.consDocs = consDocs;
    }

    public String getConsDocsSupp() {
        return consDocsSupp;
    }

    public void setConsDocsSupp(String consDocsSupp) {
        this.consDocsSupp = consDocsSupp;
    }
}
