package com.example.terramodusvtl.entities;

public enum TypeBien {

    Mais("Maison"),Villa("Villa"),Terr_Agr("Terrain Agricole"),Appart("Appartement"),
    Lot_Terr("Lot Terrain"),Comm("Local commercial"),CommImm("Local commercial"),Terr_Ur("Terrain_Urbain"),Bureau("Bureau"),
    Infra1("Infrastructure d'education"),Infra2("Infrastructure hotelière"),Infra3("Infrastructure sociale"),
    Infra4("Complexe"),Infra5("Infrastructure industrielle"),Infra6("Infrastructure commerciale"),
    Infra7("Infrastructure de santé"),Imm("Immeuble"),Lotiss("Lotissement");
    private String exactName;
    TypeBien (String exactName) {
        this.exactName = exactName;
    }

    public String getExactName() {
        return  this.exactName ;
    }
}
