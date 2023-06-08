package com.example.terramodusvtl.service;
import com.example.terramodusvtl.entities.Immob;
import com.example.terramodusvtl.entities.devisPersonne.DevisPersonneOpci;
import com.example.terramodusvtl.repositories.DemandeurPersonneRepository;
import com.example.terramodusvtl.repositories.ImmobRepository;
import com.example.terramodusvtl.repositories.TypeDevisRepository;
import com.example.terramodusvtl.repositories.devisPersonne.DevisPersonneOpciRepository;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class DevisPersonneOpciService {

    @Autowired
    private DevisPersonneOpciRepository deviseRepository;
    @Autowired
    private TypeDevisRepository typeDevisRepository;
    @Autowired
    private ImmobRepository immobRepository;
    @Autowired
    private DemandeurPersonneRepository demandeurPersonneRepository;
    @Autowired
    private EmailService emailService;


    public DevisPersonneOpci saveDevise(DevisPersonneOpci devise) throws MessagingException, jakarta.mail.MessagingException, JRException, IOException {
        System.out.println(devise.getTypeDevis() + "" + devise.getBesoin());
        devise.setTypeDevis(typeDevisRepository.getOne(devise.getTypeDevis().getId()));
        for (Immob immob: devise.getImmobs()) immobRepository.save(immob);
        demandeurPersonneRepository.save(devise.getDemandeurPersonne());
        devise.setDemandeurPersonne(demandeurPersonneRepository.getOne(devise.getDemandeurPersonne().getId()));
        if (devise.getLat() != null && devise.getLon() != null) {
            devise.setIK(DistanceCalcService.haversine(devise.getLat(), devise.getLon()));
        }
        deviseRepository.save(devise);
        String[] plaaceholders = new String[]{"NOM", "PHONE", "TID", "VAL", "VILLE", "REF", "NDEVIS", "DATE", "TV", "TITRE", "IMMOB", "ADRESSE", "DIST", "PTFrais","FP","delai","rensDoc"};

        String tid = "CIN";
        String iValue = devise.getDemandeurPersonne().getCin();
        String[] consDocs = new String[]{
                "Plan cadastral",
                "Certificat de propriété",
                "Liste des coordonnées",
                "Note de renseignements",
                "Plans autorisés et Autorisations"
        };
        String[] consDocsFournir = new String[]{};
        if(!devise.getConsDocs().isEmpty()) consDocsFournir = devise.getConsDocs().split(",");
        String FP = "";
        String delai = "10 jours ouvrables à partir de la date de validation hors délai des consultations administratives.";
        String rensDoc = "";
        System.out.println("consDocs "+consDocsFournir.length);
        if (consDocsFournir.length == 4) {
            delai = "10 jours ouvrables à partir de la date de validation";
            rensDoc = "(*) Toute la documentation nécessaire (Plan autorisée et Tableau récapitulatif des surfaces …) doit être partagée au démarrage de la mission par le client.";
        }
        else if (consDocsFournir.length == 0) FP = "(à fournir par TM*) ";

        String dist = String.valueOf((double) Math.round(devise.getIK() * 2));
        String PTFrais = String.valueOf(Math.round(((Double.valueOf(dist) * 2.5))));
        if (Double.valueOf(dist) <= 25) {
            dist = "-";
            PTFrais = "-";
        }
        String titresF = "";
        String bien = "";
        int ind = 0;
        for (Immob immob : devise.getImmobs()) {
            titresF += immob.getnTitreFoncier();
            if (ind < devise.getImmobs().size()-1)
                titresF += " et ";
            ind++;
        }
        ind = 0;
        for (Immob immob : devise.getImmobs()) {
            bien += immob.getQantite()+" "+immob.getTypeBien();
            if (immob.getQantite()>1) bien+="s";
            if (ind < devise.getImmobs().size()-1) bien += " et ";
            ind++;
        }

        System.out.println(titresF);
        String[] plaaceholdersValues = new String[]{devise.getDemandeurPersonne().getNom() + " " + devise.getDemandeurPersonne().getPrenom(), String.valueOf(devise.getDemandeurPersonne().getPhone()),
                tid, iValue, devise.getImmobs().get(0).getVille(), devise.getId() + "/" + LocalDate.now().getYear()+"/"+devise.getTypeDevis().getResponsable(), devise.getId().toString(),
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), devise.getTypeValeurEv().toString(), titresF, bien,
                devise.getImmobs().get(0).getAdresse(), dist, PTFrais ,FP,delai,rensDoc};
        String[] plaaceholdersTable = new String[]{"TV", "IMMOB", "ADRESSE"};
        String[] plaaceholdersTableValues = new String[]{devise.getTypeValeurEv().toString(), devise.getTypeBien().toString(),
                devise.getImmobs().get(0).getAdresse()};
        FileInputStream inputStream = new FileInputStream("src/main/resources/GRANDSCOMPTES.docx");
        XWPFDocument document = new XWPFDocument(inputStream);
        List<XWPFParagraph> paragraphs = document.getParagraphs();


        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String text = run.getText(0);
                for (int i = 0; i < plaaceholders.length; i++) {
                    if (text != null && text.contains(plaaceholders[i])) {
                        text = text.replace(plaaceholders[i], plaaceholdersValues[i]);
                        run.setText(text, 0);
                    }
                }
            }
        }
        // Get the first table in the document

        XWPFTable table = document.getTables().get(0);

        for (XWPFTableRow row : table.getRows()) {
            // Loop through all the cells in each row
            for (XWPFTableCell cell : row.getTableCells()) {
                // Loop through all the paragraphs in the cell
                for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    // Loop through all the runs in the paragraph
                    for (XWPFRun run : paragraph.getRuns()) {
                        // Get the text content of the run
                        String runText = run.getText(0);

                        for (int i = 0; i < plaaceholders.length; i++) {
                            if (runText!=null && runText.contains(plaaceholders[i])) {
                                runText = runText.replace(plaaceholders[i], plaaceholdersValues[i]);
                                run.setText(runText, 0);
                            }
                        }

                    }
                }
            }
        }

        for (XWPFTableRow row : table.getRows()) {
            // Loop through all the cells in each row
            for (XWPFTableCell cell : row.getTableCells()) {
                // Loop through all the paragraphs in the cell
                for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    // Loop through all the runs in the paragraph
                    for (int i = 0; i < paragraph.getRuns().size(); i++) {
                        XWPFRun run = paragraph.getRuns().get(i);
                        // Check the formatting of the run
                        if (run.getColor() != null || run.isBold()) {
                            // Get the text content of the run
                            String runText = run.getText(0);
                            for (int j = 0; j < plaaceholders.length; j++) {
                                if (runText.contains(plaaceholders[j])) {
                                    runText = runText.replace(plaaceholders[j], plaaceholdersValues[j]);
                                    run.setText(runText, 0);
                                }
                            }
                        }
                    }
                }
            }
        }

        XWPFTableRow row3 = table.getRow(7);
        XWPFTableCell cel2 = row3.getCell(2);
        for (XWPFParagraph paragraph : cel2.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String runText = run.getText(0);
                if(runText.contains("T")) {
                    runText = runText.replace("T",devise.getTypeValeurEv().toString());
                    run.setText(runText, 0);
                }
                if(runText.contains("M")) {
                    runText = runText.replace("M",bien);
                    run.setText(runText, 0);
                }
                if(runText.contains("ADRESSE")) {
                    runText = runText.replace("IMMOB",devise.getImmobs().get(0).getAdresse());
                    run.setText(runText, 0);
                }
            }
        }

        String[] livrables = new String[]{
                "Rapport d’expertise numérique (via email)",
                "Rapport d’expertise (version papier signée)",
                "Réunion de présentation ½ journée",
                "Réunion de présentation par visioconférence"
        };
        String[] Unitelivrables = new String[]{
                "Par émail",
                "Papier",
                "Physique",
                "A distance"
        };
        String[] PUHT = new String[]{
                "Inclus",
                "250.00",
                "1 500.00",
                "750.00"
        };
        String[] PTHT = new String[]{
                "Inclus",
                "250.00",
                "1 500.00",
                "750.00"
        };
        String[] livrablesDemad = devise.getLivrables().split(",");
        System.out.println("length :"+livrablesDemad.length);
        boolean[] pos = new boolean[]{false, false, false, false};
        //devise.setLivrables("");
        for (int i = 0; i < livrables.length; i++) {
            for (int j = 0; j < livrablesDemad.length; j++) {
                if (livrables[i].contains(livrablesDemad[j])) {
                    pos[i] = true;
                    //devise.setLivrables(livrables[i]+",");
                    //System.out.println("pos"+i+": "+pos[i]);
                }
            }
        }

        boolean[] pos1 = new boolean[]{false, false, false, false};
        //devise.setConsDocs("");
        for (int i = 0; i < consDocs.length; i++) {
            for (int j = 0; j < consDocsFournir.length; j++) {
                if (consDocs[i].contains(consDocsFournir[j])) {
                    System.out.println(consDocsFournir[j]+" : "+consDocs[i].contains(consDocsFournir[j]));
                    pos1[i] = true;
                    //devise.setConsDocs(consDocs[i]+",");
                    System.out.println("pos"+i+": "+pos1[i]);
                }
            }
        }
        String[] consDocsCel5 = new String[]{
                "100.00",
                "100.00",
                "15.00",
                "360.00"
        };

        XWPFTableRow row2 = table.getRow(1);
        XWPFTableCell r2cell1 = row2.getCell(0);
        XWPFTableCell r2cell5 = row2.getCell(4);

        for (XWPFParagraph paragraph : r2cell1.getParagraphs()) {
            // Loop through all the runs in the paragraph
            for (XWPFRun run : paragraph.getRuns()) {
                // Get the text content of the run
                String runText = run.getText(0);
                for (int i=1; i<=4; i++) {
                    System.out.println(i+" test------:"+consDocs[i-1]+"test------:"+pos1[i-1]);
                    for (XWPFParagraph paragraph4 : r2cell5.getParagraphs()) {
                        for (XWPFRun run4 : paragraph4.getRuns()) {
                            String runText4 = run4.getText(0);
                            System.out.println("----------"+runText4);
                            if (runText4.contains("CP"+i) && !pos1[i-1]) {
                                runText4 = runText4.replace("CP"+i, consDocsCel5[i-1]);
                                run4.setText(runText4, 0);
                            }else {
                                runText4 = runText4.replace("CP"+i, "PM");
                                run4.setText(runText4, 0);
                            }
                        }
                    }
                }

            }
        }

        XWPFTableRow row4 = table.getRow(9);
        XWPFTableCell r4cell1 = row4.getCell(0);
        XWPFTableCell r4cell5 = row4.getCell(4);

        for (XWPFParagraph paragraph : r4cell1.getParagraphs()) {
            // Loop through all the runs in the paragraph
            for (XWPFRun run : paragraph.getRuns()) {
                // Get the text content of the run
                String runText = run.getText(0);
                for (int i=1; i<=4; i++) {
                    if (runText.contains(livrables[i-1]) && pos[i-1]) {
                        for (XWPFParagraph paragraph4 : r4cell5.getParagraphs()) {
                            for (XWPFRun run4 : paragraph4.getRuns()) {
                                String runText4 = run4.getText(0);
                                System.out.println("----------"+runText4);
                                if (runText4.contains("PT"+i)) {
                                    runText4 = runText4.replace("PT"+i, PTHT[i-1]);
                                    run4.setText(runText4, 0);
                                }
                            }
                        }
                    }else {
                        for (XWPFParagraph paragraph4 : r4cell5.getParagraphs()) {
                            for (XWPFRun run4 : paragraph4.getRuns()) {
                                String runText4 = run4.getText(0);
                                if (runText4.contains("PT"+i)&&!pos[i-1]) {
                                    runText4 = runText4.replace("PT"+i, "PM");
                                    run4.setText(runText4, 0);
                                }
                            }
                        }
                    }
                }

            }
        }


        DevisPersonneOpci savedDevis = deviseRepository.save(devise);
        FileOutputStream outputStream = new FileOutputStream("src/main/resources/exported_report.docx");
        document.write(outputStream);
        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        document.write(outputStream1);
        byte[] byteArray = outputStream1.toByteArray();
        outputStream.close();
        document.close();
        outputStream1.close();
        File file = new File("src/main/resources/exported_report.docx");
        emailService.sendHtmlMessage("ouadi3laila@gmail.com", "Demande Devis", byteArray, file);
        return savedDevis;
    }
    public List<DevisPersonneOpci> findAll() {
        return deviseRepository.findAll();
    }

    public Optional<DevisPersonneOpci> findById(Long id) {
        return deviseRepository.findById(id);
    }

    public int update(DevisPersonneOpci devise){
        DevisPersonneOpci devise1 = deviseRepository.getOne(devise.getId());
        if (devise1 == null) return -1;
        else {
            deviseRepository.save(devise);
            return 1;
        }
    }

}
