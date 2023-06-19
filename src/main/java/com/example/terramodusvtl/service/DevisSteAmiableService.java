package com.example.terramodusvtl.service;

import com.example.terramodusvtl.entities.Immob;
import com.example.terramodusvtl.entities.devisSte.DevisSteAmiable;
import com.example.terramodusvtl.repositories.DemandeurSteRepository;
import com.example.terramodusvtl.repositories.ImmobRepository;
import com.example.terramodusvtl.repositories.TypeDevisRepository;
import com.example.terramodusvtl.repositories.devisSte.DevisSteAmiableRepository;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.allegro.finance.tradukisto.ValueConverters;

import javax.mail.MessagingException;
import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DevisSteAmiableService {

    @Autowired
    private DevisSteAmiableRepository deviseRepository;
    @Autowired
    private TypeDevisRepository typeDevisRepository;
    @Autowired
    private ImmobRepository immobRepository;
    @Autowired
    private DemandeurSteRepository demandeurSteRepository;
    @Autowired
    private EmailService emailService;


    public DevisSteAmiable saveDevise(DevisSteAmiable devise) throws MessagingException, jakarta.mail.MessagingException, JRException, IOException, ParseException, XmlException {
            devise.setTypeDevis(typeDevisRepository.getOne(devise.getTypeDevis().getId()));
            for (Immob immob: devise.getImmobs()) immobRepository.save(immob);
            demandeurSteRepository.save(devise.getDemandeurSte());
            devise.setDemandeurSte(demandeurSteRepository.getOne(devise.getDemandeurSte().getId()));
            if (devise.getLat() != null && devise.getLon() != null) {
                devise.setIK(DistanceCalcService.haversine(devise.getLat(), devise.getLon()));
            }
            deviseRepository.save(devise);
    
            String[] plaaceholders = new String[]{"VILLE", "REF", "NDEVIS", "DATE", "TEV", "TITRE", "IMMOB", "ADRESSE", "DIST", "PTFrais","PEI","PrixChiffres"};
            String[] headerPlaceholders = new String[]{"NOM", "PHONE", "TID", "VAL","IDENTM","SIGE"};
    
            String tid = "ICE";
            String iValue = devise.getDemandeurSte().getIce();
            String[] consDocs = new String[]{
                    "-       Certificat de propriété",
                    "-       Plan de propriété",
                    "-       Calcul de Contenance",
                    "-       Note de renseignement"
            };
            String[] consDocsUnite = new String[]{
                    "Certificat",
                    "Plan",
                    "CC",
                    "Document"
            };
            String[] consDocsCel5 = new String[]{
                    "100.00",
                    "100.00",
                    "15.00",
                    "360.00"
            };
            String[] consDocsSupp = new String[]{
                    "-       Cahier des charges du lotissement",
                    "-       Plan architecte ne variateur",
                    "-       Autorisation de construire",
                    "-       Permis d'habiter",
                    "-       Tableau récapitulatif des surfaces"
            };
            String[] consDocsSuppUnite = new String[]{
                    "Document",
                    "Plan",
                    "Document",
                    "Document",
                    "Document"
            };
    
            switch (devise.getImmobs().get(0).getTypeBien()) {
                case Appart,Bureau,CommImm -> {
                    consDocs = new String[]{
                            "-       Certificat de copropriété",
                            "-       Plan de copropriété",
                            "-       Règlement de copropriété"
                    };
                    consDocsUnite = new String[]{
                            "Certificat",
                            "Plan",
                            "Document"
                    };
                    consDocsCel5 = new String[]{
                            "100.00",
                            "100.00",
                            "1200.00"
                    };
                }
                case Mais,Villa -> {
                    consDocs = new String[]{
                            "-       Certificat de propriété",
                            "-       Plan cadastral",
                            "-       Calcul de Contenance",
                            "-       Note de renseignement"
                    };
                    consDocsUnite = new String[]{
                            "Certificat",
                            "Plan",
                            "CC",
                            "Note"
                    };
                    consDocsCel5 = new String[]{
                            "100.00",
                            "100.00",
                            "15.00",
                            "360.00"
                    };
                }
                case Comm -> {
                    consDocs = new String[]{
                            "-       Certificat de propriété",
                            "-       Plan cadastral",
                            "-       Règlement de copropriété"
                    };
                    consDocsUnite = new String[]{
                            "Certificat",
                            "Plan",
                            "Document"
                    };
                    consDocsCel5 = new String[]{
                            "100.00",
                            "100.00",
                            "1200.00"
                    };
                }
                case Terr_Ur,Terr_Agr,Lot_Terr,Lotiss -> {
                    consDocs = new String[]{
                            "-       Certificat de propriété",
                            "-       Plan cadastral",
                            "-       Note de renseignement"
                    };
                    consDocsUnite = new String[]{
                            "Certificat",
                            "Plan",
                            "Note"
                    };
                    consDocsCel5 = new String[]{
                            "100.00",
                            "100.00",
                            "360.00"
                    };
                }
            }
    
            String[] consDocsFournir = new String[]{};
            if(devise.getConsDocs()!=null && !devise.getConsDocs().isEmpty()) consDocsFournir = devise.getConsDocs().split(",");
    
            String[] consDocsSuppFournir = new String[]{};
            if(devise.getConsDocsSupp()!=null  && !devise.getConsDocsSupp().isEmpty())
                consDocsSuppFournir = devise.getConsDocsSupp().split(",");
    
            DecimalFormat df = new DecimalFormat("#,###.00", new DecimalFormatSymbols(Locale.US));
            double distV = Math.round(devise.getIK() * 2);
            String dist = df.format(distV);
            String PTFrais = df.format(distV * 2.5).replace(',', ' ');
            String PEI = "";
            int q = devise.getImmobs().get(0).getQantite();
            switch (devise.getImmobs().get(0).getTypeBien()) {
                case Appart -> PEI = df.format(q*2000.00).replace(',', ' ');
                case Bureau,Lot_Terr,Comm,CommImm ->  PEI = df.format(q*2500.00).replace(',', ' ');
                case Villa,Mais -> PEI = df.format(q*3000.00).replace(',', ' ');
                case Terr_Agr -> {
                    if (devise.getImmobs().get(0).getSuperficie() < 10000) PEI = df.format(q*3500.00).replace(',', ' ');
                    else if (devise.getImmobs().get(0).getSuperficie() < 50000 && devise.getImmobs().get(0).getSuperficie() >= 10000) PEI = df.format(q*5000.00).replace(',', ' ');
                    else if (devise.getImmobs().get(0).getSuperficie() < 100000 && devise.getImmobs().get(0).getSuperficie() >= 50000) PEI = df.format(q*8000.00).replace(',', ' ');
                    else if (devise.getImmobs().get(0).getSuperficie() < 1000000 && devise.getImmobs().get(0).getSuperficie() >= 100000) PEI = df.format(q*12000.00).replace(',', ' ');
                    else PEI = df.format(q*20000.00).replace(',', ' ');
                }
                case Terr_Ur -> {
                    if (devise.getImmobs().get(0).getSuperficie() < 10000) PEI = df.format(q*5000.00).replace(',', ' ');
                    else if (devise.getImmobs().get(0).getSuperficie() < 50000 && devise.getImmobs().get(0).getSuperficie() >= 10000) PEI = df.format(q*8000.00).replace(',', ' ');
                    else if (devise.getImmobs().get(0).getSuperficie() < 100000 && devise.getImmobs().get(0).getSuperficie() >= 50000) PEI = df.format(q*10000.00).replace(',', ' ');
                    else if (devise.getImmobs().get(0).getSuperficie() < 1000000 && devise.getImmobs().get(0).getSuperficie() >= 100000) PEI = df.format(q*15000.00).replace(',', ' ');
                    else PEI = df.format(q*25000.00).replace(',', ' ');
                }
                case Infra1,Infra2,Infra3,Infra4,Infra5,Infra6,Infra7 -> {
                    if (devise.getImmobs().get(0).getSuperficie() < 500) PEI = df.format(q*5000.00).replace(',', ' ');
                    else if (devise.getImmobs().get(0).getSuperficie() < 1000 && devise.getImmobs().get(0).getSuperficie() >= 500) PEI = df.format(q*7000.00).replace(',', ' ');
                    else if (devise.getImmobs().get(0).getSuperficie() < 10000 && devise.getImmobs().get(0).getSuperficie() >= 1000) PEI = df.format(q*10000.00).replace(',', ' ');
                    else PEI = df.format(q*15000.00).replace(',', ' ');
                }
                case Lotiss,Imm -> {
                    if (devise.getImmobs().get(0).getnActifs().equals("moins10")) PEI = df.format(q*5000.00).replace(',', ' ');
                    else if (devise.getImmobs().get(0).getnActifs().equals("entre11et30")) PEI = df.format(q*7500.00).replace(',', ' ');
                    else if (devise.getImmobs().get(0).getnActifs().equals("entre31et50")) PEI = df.format(q*10000.00).replace(',', ' ');
                    else if (devise.getImmobs().get(0).getnActifs().equals("plus50")) PEI = df.format(q*15000.00).replace(',', ' ');
                }
            }
            Double HT = 0.00;
            Double TVA;
            Double PTTC;
            if (distV <= 25) {
                dist = "-";
                PTFrais = "-";
                HT = Double.valueOf(PEI.replaceAll("\\s", ""));
            }else {
                HT = Double.valueOf(PEI.replaceAll("\\s", "")) + df.parse(PTFrais.replace(' ', ',')).doubleValue();
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
                bien += new DecimalFormat("00").format(immob.getQantite())+" "+immob.getTypeBien().getExactName();
                if (immob.getQantite()>1) bien+="s";
                if (ind < devise.getImmobs().size()-1) bien += " et ";
                ind++;
            }
    
            String[] plaaceholdersValues = new String[]{devise.getImmobs().get(0).getVille(), devise.getId() + "/" + LocalDate.now().getYear()+"/"+devise.getTypeDevis().getResponsable(), devise.getId().toString(),
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), devise.getTypeValeurEv().getAbreviation().toString(), titresF, bien,
                    devise.getImmobs().get(0).getAdresse(), dist, PTFrais ,PEI,"PrixChiffres"};

            String[] headerPlaceholdersVal = new String[]{devise.getDemandeurSte().getNomSte(), String.valueOf(devise.getDemandeurSte().getPhone())
                    ,tid ,iValue, devise.getDemandeurSte().getEmail(),devise.getDemandeurSte().getSiege()};

            //String[] plaaceholdersTable = new String[]{"TV", "IMMOB", "ADRESSE"};
            //String[] plaaceholdersTableValues = new String[]{devise.getTypeValeurEv().getAbreviation().toString(), devise.getImmobs().get(0).getTypeBien().toString(),
            //        devise.getImmobs().get(0).getAdresse()};
            FileInputStream inputStream = new FileInputStream("src/main/resources/AmiableSte.docx");
            XWPFDocument document = new XWPFDocument(inputStream);
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            for (XWPFParagraph paragraph : paragraphs) {
                XmlCursor cursor = paragraph.getCTP().newCursor();
                cursor.selectPath("declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' .//*/w:txbxContent/w:p/w:r");

                List<XmlObject> ctrsintxtbx = new ArrayList<XmlObject>();

                while(cursor.hasNextSelection()) {
                    cursor.toNextSelection();
                    XmlObject obj = cursor.getObject();
                    ctrsintxtbx.add(obj);
                }
                for (XmlObject obj : ctrsintxtbx) {
                    CTR ctr = CTR.Factory.parse(obj.xmlText());
                    //CTR ctr = CTR.Factory.parse(obj.newInputStream());
                    XWPFRun bufferrun = new XWPFRun(ctr, (IRunBody)paragraph);
                    String text = bufferrun.getText(0);
                    for (int i = 0; i < headerPlaceholders.length; i++) {
                        if (text != null && text.contains(headerPlaceholders[i])) {
                            if (plaaceholdersValues[i]!=null) {
                                text = text.replace(headerPlaceholders[i], headerPlaceholdersVal[i]);
                                bufferrun.setText(text, 0);
                            }
                        }
                    }
                    obj.set(bufferrun.getCTR());
                }
            }

            for (XWPFParagraph paragraph : paragraphs) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    for (int i = 0; i < plaaceholders.length; i++) {
                        if (text != null && text.contains(plaaceholders[i])) {
                            if (plaaceholdersValues[i]!=null) {
                                text = text.replace(plaaceholders[i], plaaceholdersValues[i]);
                                run.setText(text, 0);
                            }
                        }
                    }
                }
            }

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
                                if (runText.contains(plaaceholders[i])) {
                                    if (plaaceholdersValues[i] != null) {
                                        runText = runText.replace(plaaceholders[i], plaaceholdersValues[i]);
                                        run.setText(runText, 0);
                                    }
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

            XWPFTableRow row4 = table.getRow(3);
            XWPFTableCell cel2 = row4.getCell(2);
            for (XWPFParagraph paragraph : cel2.getParagraphs()) {
                for (XWPFRun run : paragraph.getRuns()) {
                    String runText = run.getText(0);
                    if(runText.contains("TEV")) {
                        runText = runText.replace("TEV",devise.getTypeValeurEv().getAbreviation().toString());
                        run.setText(runText, 0);
                    }
                    if(runText.contains("(B)")) {
                        runText = runText.replace("(B)",bien);
                        run.setText(runText, 0);
                    }
                    if(runText.contains("ADRESSE")) {
                        runText = runText.replace("ADRESSE",devise.getImmobs().get(0).getAdresse());
                        run.setText(runText, 0);
                    }
                    if(runText.contains("VILLE")) {
                        runText = runText.replace("VILLE",devise.getImmobs().get(0).getVille());
                        run.setText(runText, 0);
                    }
                    if(runText.contains("SUPERF")) {
                        runText = runText.replace("SUPERF", String.valueOf(Math.round(devise.getImmobs().get(0).getSuperficie())) + " m²");
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
    
            boolean[] pos1 = new boolean[consDocs.length];

            for (int i = 0; i < consDocs.length; i++) {
                for (int j = 0; j < consDocsFournir.length; j++) {
                    if (consDocs[i].contains(consDocsFournir[j])) pos1[i] = true;
                }
            }
            boolean[] pos2 = new boolean[consDocsSupp.length];
            for (int i = 0; i < consDocsSupp.length; i++) {
                for (int j = 0; j < consDocsSuppFournir.length; j++) {
                    if (consDocsSupp[i].contains(consDocsSuppFournir[j])) pos2[i] = true;
                }
            }
    
            //modification premiere ligne : phase1
            XWPFTableRow row2 = table.getRow(1);
            XWPFTableCell r2cell1 = row2.getCell(0);
            XWPFTableCell r2cell2 = row2.getCell(1);
            XWPFTableCell r2cell3 = row2.getCell(2);
            XWPFTableCell r2cell4 = row2.getCell(3);
            XWPFTableCell r2cell5 = row2.getCell(4);
            boolean ftpos1[] = pos1;
            String[] consDocs1 = consDocs;
            String[] consDocsUnite1 = consDocsUnite;
            String[] consDocsCel5Cop1 = consDocsCel5;
            int index = 1;
            int indForDel = consDocs1.length;
            boolean restartLoop1 = false;
            while (true) {
                //System.out.println(i+" test------:"+consDocs[i-1]+"test------:"+pos1[i-1]);
                outerloop:
                for (XWPFParagraph paragraph1 : r2cell1.getParagraphs()) {
                    for (XWPFRun run1 : paragraph1.getRuns()) {
                        String runText1 = run1.getText(0);
                        System.out.println("----------"+runText1);
                        if (runText1.contains("CA"+index) && !ftpos1[0]) {
                            runText1 = runText1.replace("CA"+index, consDocs1[0]);
                            run1.setText(runText1, 0);
    
                            for (XWPFParagraph paragraph2 : r2cell2.getParagraphs()) {
                                for (XWPFRun run2 : paragraph2.getRuns()) {
                                    String runText2 = run2.getText(0);
                                    if (runText2.contains("Q"+index)) {
                                        runText2 = runText2.replace("Q"+index, new DecimalFormat("00").format(q));
                                        run2.setText(runText2, 0);
                                    }
                                }
                            }
                            for (XWPFParagraph paragraph2 : r2cell3.getParagraphs()) {
                                for (XWPFRun run2 : paragraph2.getRuns()) {
                                    String runText2 = run2.getText(0);
                                    if (runText2.contains("CU"+index)) {
                                        runText2 = runText2.replace("CU"+index, consDocsUnite1[0]);
                                        run2.setText(runText2, 0);
                                    }
                                }
                            }
                            for (XWPFParagraph paragraph2 : r2cell4.getParagraphs()) {
                                for (XWPFRun run2 : paragraph2.getRuns()) {
                                    String runText2 = run2.getText(0);
                                    if (runText2.contains("CPU"+index)) {
                                        runText2 = runText2.replace("CPU"+index, consDocsCel5Cop1[0]);
                                        run2.setText(runText2, 0);
                                    }
                                }
                            }
                            for (XWPFParagraph paragraph2 : r2cell5.getParagraphs()) {
                                for (XWPFRun run2 : paragraph2.getRuns()) {
                                    String runText2 = run2.getText(0);
                                    if (runText2.contains("CP"+index)) {
                                        runText2 = runText2.replace("CP"+index, String.valueOf(new DecimalFormat(".00",new DecimalFormatSymbols(Locale.US)).format(Double.parseDouble(consDocsCel5Cop1[0]) * q)));
                                        if (!consDocsCel5Cop1[0].equals("--"))
                                            HT += Double.valueOf(consDocsCel5Cop1[0]) * q;
                                        run2.setText(runText2, 0);
                                    }
                                }
                            }
                            ArrayList<Boolean> arrayList = new ArrayList<>();
                            for (boolean value : ftpos1) arrayList.add(value);
                            arrayList.remove(0);
                            boolean[] newPos1 = new boolean[arrayList.size()];
                            for (int k = 0; k < newPos1.length; k++) {
                                newPos1[k] = arrayList.get(k);
                            }
                            ftpos1 = newPos1;
                            ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(consDocs1));
                            arrayList1.remove(0);
                            consDocs1 = arrayList1.toArray(new String[0]);
                            ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(consDocsUnite1));
                            arrayList2.remove(0);
                            consDocsUnite1 = arrayList2.toArray(new String[0]);
                            ArrayList<String> arrayList3 = new ArrayList<>(Arrays.asList(consDocsCel5Cop1));
                            arrayList3.remove(0);
                            consDocsCel5Cop1 = arrayList3.toArray(new String[0]);
                            index++;
                            restartLoop1 = false;
                            if (index > consDocs.length) break outerloop;
                        } else if (runText1.contains("CA"+index) && ftpos1[0]) {
                            List<XWPFParagraph> paragraphsToRemove1 = new ArrayList<>();
                            for (XWPFParagraph paragraph : r2cell1.getParagraphs()) {
                                String paragraphText = paragraph.getText();
                                if (paragraphText.contains("CA"+indForDel)) {
                                    paragraphsToRemove1.add(paragraph);
                                }
                            }
                            for (XWPFParagraph paragraph : paragraphsToRemove1) {
                                r2cell1.removeParagraph(r2cell1.getParagraphs().indexOf(paragraph));
                            }
                            List<XWPFParagraph> paragraphsToRemove2 = new ArrayList<>();
                            for (XWPFParagraph paragraph : r2cell2.getParagraphs()) {
                                String paragraphText = paragraph.getText();
                                if (paragraphText.contains("Q"+indForDel)) {
                                    paragraphsToRemove2.add(paragraph);
                                }
                            }
                            for (XWPFParagraph paragraph : paragraphsToRemove2) {
                                r2cell2.removeParagraph(r2cell2.getParagraphs().indexOf(paragraph));
                            }
                            List<XWPFParagraph> paragraphsToRemove3 = new ArrayList<>();
                            for (XWPFParagraph paragraph : r2cell3.getParagraphs()) {
                                String paragraphText = paragraph.getText();
                                if (paragraphText.contains("CU"+indForDel)) {
                                    paragraphsToRemove3.add(paragraph);
                                }
                            }
                            for (XWPFParagraph paragraph : paragraphsToRemove3) {
                                r2cell3.removeParagraph(r2cell3.getParagraphs().indexOf(paragraph));
                            }
                            List<XWPFParagraph> paragraphsToRemove4 = new ArrayList<>();
                            for (XWPFParagraph paragraph : r2cell4.getParagraphs()) {
                                String paragraphText = paragraph.getText();
                                if (paragraphText.contains("CPU"+indForDel)) {
                                    paragraphsToRemove4.add(paragraph);
                                }
                            }
                            for (XWPFParagraph paragraph : paragraphsToRemove4) {
                                r2cell4.removeParagraph(r2cell4.getParagraphs().indexOf(paragraph));
                            }
                            List<XWPFParagraph> paragraphsToRemove5 = new ArrayList<>();
                            for (XWPFParagraph paragraph : r2cell5.getParagraphs()) {
                                String paragraphText = paragraph.getText();
                                if (paragraphText.contains("CP"+indForDel)) {
                                    paragraphsToRemove5.add(paragraph);
                                }
                            }
                            for (XWPFParagraph paragraph : paragraphsToRemove5) {
                                r2cell5.removeParagraph(r2cell5.getParagraphs().indexOf(paragraph));
                            }
                            ArrayList<Boolean> arrayList = new ArrayList<>();
                            for (boolean value : ftpos1) arrayList.add(value);
                            arrayList.remove(0);
                            boolean[] newPos1 = new boolean[arrayList.size()];
                            for (int k = 0; k < newPos1.length; k++) {
                                newPos1[k] = arrayList.get(k);
                            }
                            ftpos1 = newPos1;
                            ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(consDocs1));
                            arrayList1.remove(0);
                            consDocs1 = arrayList1.toArray(new String[0]);
                            ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(consDocsUnite1));
                            arrayList2.remove(0);
                            consDocsUnite1 = arrayList2.toArray(new String[0]);
                            ArrayList<String> arrayList3 = new ArrayList<>(Arrays.asList(consDocsCel5Cop1));
                            arrayList3.remove(0);
                            consDocsCel5Cop1 = arrayList3.toArray(new String[0]);
                            if (indForDel == 1) restartLoop1 = false;
                            else restartLoop1 = true;
                            indForDel--;
                            break outerloop;
                        }else {
                            restartLoop1 = false;
                        }
                    }
                }
                if (!restartLoop1) {
                    break;
                }
            }
            for (int k=1;k<=4;k++) {
                List<XWPFParagraph> paragraphsToRemove1 = new ArrayList<>();
                for (XWPFParagraph paragraph : r2cell1.getParagraphs()) {
                    String paragraphText = paragraph.getText();
                    if (paragraphText.contains("CA"+k)) {
                        paragraphsToRemove1.add(paragraph);
                    }
                }
                for (XWPFParagraph paragraph : paragraphsToRemove1) {
                    r2cell1.removeParagraph(r2cell1.getParagraphs().indexOf(paragraph));
                }
                List<XWPFParagraph> paragraphsToRemove2 = new ArrayList<>();
                for (XWPFParagraph paragraph : r2cell2.getParagraphs()) {
                    String paragraphText = paragraph.getText();
                    if (paragraphText.contains("Q"+k)) {
                        paragraphsToRemove2.add(paragraph);
                    }
                }
                for (XWPFParagraph paragraph : paragraphsToRemove2) {
                    r2cell2.removeParagraph(r2cell2.getParagraphs().indexOf(paragraph));
                }
                List<XWPFParagraph> paragraphsToRemove3 = new ArrayList<>();
                for (XWPFParagraph paragraph : r2cell3.getParagraphs()) {
                    String paragraphText = paragraph.getText();
                    if (paragraphText.contains("CU"+k)) {
                        paragraphsToRemove3.add(paragraph);
                    }
                }
                for (XWPFParagraph paragraph : paragraphsToRemove3) {
                    r2cell3.removeParagraph(r2cell3.getParagraphs().indexOf(paragraph));
                }
                List<XWPFParagraph> paragraphsToRemove4 = new ArrayList<>();
                for (XWPFParagraph paragraph : r2cell4.getParagraphs()) {
                    String paragraphText = paragraph.getText();
                    if (paragraphText.contains("CPU"+k)) {
                        paragraphsToRemove4.add(paragraph);
                    }
                }
                for (XWPFParagraph paragraph : paragraphsToRemove4) {
                    r2cell4.removeParagraph(r2cell4.getParagraphs().indexOf(paragraph));
                }
                List<XWPFParagraph> paragraphsToRemove5 = new ArrayList<>();
                for (XWPFParagraph paragraph : r2cell5.getParagraphs()) {
                    String paragraphText = paragraph.getText();
                    if (paragraphText.contains("CP"+k)) {
                        paragraphsToRemove5.add(paragraph);
                    }
                }
                for (XWPFParagraph paragraph : paragraphsToRemove5) {
                    r2cell5.removeParagraph(r2cell5.getParagraphs().indexOf(paragraph));
                }
            }
            //modification deuxième ligne : phase2
            XWPFTableRow row3 = table.getRow(2);
            XWPFTableCell r3cell1 = row3.getCell(0);
            XWPFTableCell r3cell2 = row3.getCell(1);
            XWPFTableCell r3cell3 = row3.getCell(2);
            XWPFTableCell r3cell4 = row3.getCell(3);
            XWPFTableCell r3cell5 = row3.getCell(4);
            boolean ftpos2[] = pos1;
            String consDocs2[] = consDocs;
            String consDocsUnite2[] = consDocsUnite;
            int index1 = 1;
            indForDel = consDocs2.length;
            boolean restartLoop2 = false;
            while (true) {
                //System.out.println(i+" test------:"+consDocs[i-1]+"test------:"+pos1[i-1]);
                outerloop:
                for (XWPFParagraph paragraph1 : r3cell1.getParagraphs()) {
                    for (XWPFRun run1 : paragraph1.getRuns()) {
                        String runText1 = run1.getText(0);
                        System.out.println("----------"+runText1);
                        if (runText1.contains("CA"+index1) && ftpos2[0]) {
                            runText1 = runText1.replace("CA"+index1, consDocs2[0]);
                            run1.setText(runText1, 0);
    
                            for (XWPFParagraph paragraph2 : r3cell2.getParagraphs()) {
                                for (XWPFRun run2 : paragraph2.getRuns()) {
                                    String runText2 = run2.getText(0);
                                    if (runText2.contains("Q"+index1)) {
                                        runText2 = runText2.replace("Q"+index1, new DecimalFormat("00").format(q));
                                        run2.setText(runText2, 0);
                                    }
                                }
                            }
                            for (XWPFParagraph paragraph2 : r3cell3.getParagraphs()) {
                                for (XWPFRun run2 : paragraph2.getRuns()) {
                                    String runText2 = run2.getText(0);
                                    if (runText2.contains("CU"+index1)) {
                                        runText2 = runText2.replace("CU"+index1, consDocsUnite2[0]);
                                        run2.setText(runText2, 0);
                                    }
                                }
                            }
                            for (XWPFParagraph paragraph2 : r3cell4.getParagraphs()) {
                                for (XWPFRun run2 : paragraph2.getRuns()) {
                                    String runText2 = run2.getText(0);
                                    if (runText2.contains("CPU"+index1)) {
                                        runText2 = runText2.replace("CPU"+index1, "--");
                                        run2.setText(runText2, 0);
                                    }
                                }
                            }
                            for (XWPFParagraph paragraph2 : r3cell5.getParagraphs()) {
                                for (XWPFRun run2 : paragraph2.getRuns()) {
                                    String runText2 = run2.getText(0);
                                    if (runText2.contains("CP"+index1)) {
                                        runText2 = runText2.replace("CP"+index1, "--");
                                        run2.setText(runText2, 0);
                                    }
                                }
                            }

                            ArrayList<Boolean> arrayList = new ArrayList<>();
                            for (boolean value : ftpos2) arrayList.add(value);
                            arrayList.remove(0);
                            boolean[] newPos2 = new boolean[arrayList.size()];
                            for (int k = 0; k < newPos2.length; k++) {
                                newPos2[k] = arrayList.get(k);
                            }
                            ftpos2 = newPos2;
                            ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(consDocs2));
                            arrayList1.remove(0);
                            consDocs2 = arrayList1.toArray(new String[0]);
                            ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(consDocsUnite2));
                            arrayList2.remove(0);
                            consDocsUnite2 = arrayList2.toArray(new String[0]);
                            index1++;
                            restartLoop2 = false;
                            if (index1 > consDocs.length) break outerloop;
                        }else if (runText1.contains("CA"+index1) && !ftpos2[0]) {
                            List<XWPFParagraph> paragraphsToRemove1 = new ArrayList<>();
                            for (XWPFParagraph paragraph : r3cell1.getParagraphs()) {
                                String paragraphText = paragraph.getText();
                                if (paragraphText.contains("CA"+indForDel)) {
                                    paragraphsToRemove1.add(paragraph);
                                }
                            }
                            for (XWPFParagraph paragraph : paragraphsToRemove1) {
                                r3cell1.removeParagraph(r3cell1.getParagraphs().indexOf(paragraph));
                            }
                            List<XWPFParagraph> paragraphsToRemove2 = new ArrayList<>();
                            for (XWPFParagraph paragraph : r3cell2.getParagraphs()) {
                                String paragraphText = paragraph.getText();
                                if (paragraphText.contains("Q"+indForDel)) {
                                    paragraphsToRemove2.add(paragraph);
                                }
                            }
                            for (XWPFParagraph paragraph : paragraphsToRemove2) {
                                r3cell2.removeParagraph(r3cell2.getParagraphs().indexOf(paragraph));
                            }
                            List<XWPFParagraph> paragraphsToRemove3 = new ArrayList<>();
                            for (XWPFParagraph paragraph : r3cell3.getParagraphs()) {
                                String paragraphText = paragraph.getText();
                                if (paragraphText.contains("CU"+indForDel)) {
                                    paragraphsToRemove3.add(paragraph);
                                }
                            }
                            for (XWPFParagraph paragraph : paragraphsToRemove3) {
                                r3cell3.removeParagraph(r3cell3.getParagraphs().indexOf(paragraph));
                            }
                            List<XWPFParagraph> paragraphsToRemove4 = new ArrayList<>();
                            for (XWPFParagraph paragraph : r3cell4.getParagraphs()) {
                                String paragraphText = paragraph.getText();
                                if (paragraphText.contains("CPU"+indForDel)) {
                                    paragraphsToRemove4.add(paragraph);
                                }
                            }
                            for (XWPFParagraph paragraph : paragraphsToRemove4) {
                                r3cell4.removeParagraph(r3cell4.getParagraphs().indexOf(paragraph));
                            }
                            List<XWPFParagraph> paragraphsToRemove5 = new ArrayList<>();
                            for (XWPFParagraph paragraph : r3cell5.getParagraphs()) {
                                String paragraphText = paragraph.getText();
                                if (paragraphText.contains("CP"+indForDel)) {
                                    paragraphsToRemove5.add(paragraph);
                                }
                            }
                            for (XWPFParagraph paragraph : paragraphsToRemove5) {
                                r3cell5.removeParagraph(r3cell5.getParagraphs().indexOf(paragraph));
                            }
                            ArrayList<Boolean> arrayList = new ArrayList<>();
                            for (boolean value : ftpos2) arrayList.add(value);
                            arrayList.remove(0);
                            boolean[] newPos2 = new boolean[arrayList.size()];
                            for (int k = 0; k < newPos2.length; k++) {
                                newPos2[k] = arrayList.get(k);
                            }
                            ftpos2 = newPos2;
                            ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(consDocs2));
                            arrayList1.remove(0);
                            consDocs2 = arrayList1.toArray(new String[0]);
                            ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(consDocsUnite2));
                            arrayList2.remove(0);
                            consDocsUnite2 = arrayList2.toArray(new String[0]);
                            if (indForDel == 1) restartLoop2 = false;
                            else restartLoop2 = true;
                            indForDel--;
                            break outerloop;
                        }else {
                            restartLoop2 = false;
                        }
                    }
                }
                if (!restartLoop2) {
                    break;
                }
            }
            for (int k=1;k<=4;k++) {
                List<XWPFParagraph> paragraphsToRemove1 = new ArrayList<>();
                for (XWPFParagraph paragraph : r3cell1.getParagraphs()) {
                    String paragraphText = paragraph.getText();
                    if (paragraphText.contains("CA"+k)) {
                        paragraphsToRemove1.add(paragraph);
                    }
                }
                for (XWPFParagraph paragraph : paragraphsToRemove1) {
                    r3cell1.removeParagraph(r3cell1.getParagraphs().indexOf(paragraph));
                }
                List<XWPFParagraph> paragraphsToRemove2 = new ArrayList<>();
                for (XWPFParagraph paragraph : r3cell2.getParagraphs()) {
                    String paragraphText = paragraph.getText();
                    if (paragraphText.contains("Q"+k)) {
                        paragraphsToRemove2.add(paragraph);
                    }
                }
                for (XWPFParagraph paragraph : paragraphsToRemove2) {
                    r3cell2.removeParagraph(r3cell2.getParagraphs().indexOf(paragraph));
                }
                List<XWPFParagraph> paragraphsToRemove3 = new ArrayList<>();
                for (XWPFParagraph paragraph : r3cell3.getParagraphs()) {
                    String paragraphText = paragraph.getText();
                    if (paragraphText.contains("CU"+k)) {
                        paragraphsToRemove3.add(paragraph);
                    }
                }
                for (XWPFParagraph paragraph : paragraphsToRemove3) {
                    r3cell3.removeParagraph(r3cell3.getParagraphs().indexOf(paragraph));
                }
                List<XWPFParagraph> paragraphsToRemove4 = new ArrayList<>();
                for (XWPFParagraph paragraph : r3cell4.getParagraphs()) {
                    String paragraphText = paragraph.getText();
                    if (paragraphText.contains("CPU"+k)) {
                        paragraphsToRemove4.add(paragraph);
                    }
                }
                for (XWPFParagraph paragraph : paragraphsToRemove4) {
                    r3cell4.removeParagraph(r3cell4.getParagraphs().indexOf(paragraph));
                }
                List<XWPFParagraph> paragraphsToRemove5 = new ArrayList<>();
                for (XWPFParagraph paragraph : r3cell5.getParagraphs()) {
                    String paragraphText = paragraph.getText();
                    if (paragraphText.contains("CP"+k)) {
                        paragraphsToRemove5.add(paragraph);
                    }
                }
                for (XWPFParagraph paragraph : paragraphsToRemove5) {
                    r3cell5.removeParagraph(r3cell5.getParagraphs().indexOf(paragraph));
                }
            }
            index1 = 1;
            indForDel = consDocsSupp.length;
            restartLoop2 = false;
            while (true) {
                //System.out.println(i+" test------:"+consDocs[i-1]+"test------:"+pos1[i-1]);
                outerloop:
                for (XWPFParagraph paragraph1 : r3cell1.getParagraphs()) {
                    for (XWPFRun run1 : paragraph1.getRuns()) {
                        String runText1 = run1.getText(0);
                        System.out.println("----------"+runText1);
                        if (runText1.contains("CS"+index1) && pos2[0]) {
                            runText1 = runText1.replace("CS"+index1, consDocsSupp[0]);
                            run1.setText(runText1, 0);
    
                            for (XWPFParagraph paragraph2 : r3cell2.getParagraphs()) {
                                for (XWPFRun run2 : paragraph2.getRuns()) {
                                    String runText2 = run2.getText(0);
                                    if (runText2.contains("QS"+index1)) {
                                        runText2 = runText2.replace("QS"+index1, new DecimalFormat("00").format(q));
                                        run2.setText(runText2, 0);
                                    }
                                }
                            }
                            for (XWPFParagraph paragraph2 : r3cell3.getParagraphs()) {
                                for (XWPFRun run2 : paragraph2.getRuns()) {
                                    String runText2 = run2.getText(0);
                                    if (runText2.contains("CUS"+index1)) {
                                        runText2 = runText2.replace("CUS"+index1, consDocsSuppUnite[0]);
                                        run2.setText(runText2, 0);
                                    }
                                }
                            }
                            for (XWPFParagraph paragraph2 : r3cell4.getParagraphs()) {
                                for (XWPFRun run2 : paragraph2.getRuns()) {
                                    String runText2 = run2.getText(0);
                                    if (runText2.contains("CPUS"+index1)) {
                                        runText2 = runText2.replace("CPUS"+index1, "--");
                                        run2.setText(runText2, 0);
                                    }
                                }
                            }
                            for (XWPFParagraph paragraph2 : r3cell5.getParagraphs()) {
                                for (XWPFRun run2 : paragraph2.getRuns()) {
                                    String runText2 = run2.getText(0);
                                    if (runText2.contains("CPS"+index1)) {
                                        runText2 = runText2.replace("CPS"+index1, "--");
                                        run2.setText(runText2, 0);
                                    }
                                }
                            }
                            ArrayList<Boolean> arrayList = new ArrayList<>();
                            for (boolean value : pos2) arrayList.add(value);
                            arrayList.remove(0);
                            boolean[] newPos2 = new boolean[arrayList.size()];
                            for (int k = 0; k < newPos2.length; k++) {
                                newPos2[k] = arrayList.get(k);
                            }
                            pos2 = newPos2;
                            ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(consDocsSupp));
                            arrayList1.remove(0);
                            consDocsSupp = arrayList1.toArray(new String[0]);
                            ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(consDocsSuppUnite));
                            arrayList2.remove(0);
                            consDocsSuppUnite = arrayList2.toArray(new String[0]);
                            index1++;
                            restartLoop2 = false;
                        }else if (runText1.contains("CS"+index1) && !pos2[0]) {
                            List<XWPFParagraph> paragraphsToRemove1 = new ArrayList<>();
                            for (XWPFParagraph paragraph : r3cell1.getParagraphs()) {
                                String paragraphText = paragraph.getText();
                                if (paragraphText.contains("CS"+indForDel)) {
                                    paragraphsToRemove1.add(paragraph);
                                }
                            }
                            for (XWPFParagraph paragraph : paragraphsToRemove1) {
                                r3cell1.removeParagraph(r3cell1.getParagraphs().indexOf(paragraph));
                            }
                            List<XWPFParagraph> paragraphsToRemove2 = new ArrayList<>();
                            for (XWPFParagraph paragraph : r3cell2.getParagraphs()) {
                                String paragraphText = paragraph.getText();
                                if (paragraphText.contains("QS"+indForDel)) {
                                    paragraphsToRemove2.add(paragraph);
                                }
                            }
                            for (XWPFParagraph paragraph : paragraphsToRemove2) {
                                r3cell2.removeParagraph(r3cell2.getParagraphs().indexOf(paragraph));
                            }
                            List<XWPFParagraph> paragraphsToRemove3 = new ArrayList<>();
                            for (XWPFParagraph paragraph : r3cell3.getParagraphs()) {
                                String paragraphText = paragraph.getText();
                                if (paragraphText.contains("CUS"+indForDel)) {
                                    paragraphsToRemove3.add(paragraph);
                                }
                            }
                            for (XWPFParagraph paragraph : paragraphsToRemove3) {
                                r3cell3.removeParagraph(r3cell3.getParagraphs().indexOf(paragraph));
                            }
                            List<XWPFParagraph> paragraphsToRemove4 = new ArrayList<>();
                            for (XWPFParagraph paragraph : r3cell4.getParagraphs()) {
                                String paragraphText = paragraph.getText();
                                if (paragraphText.contains("CPUS"+indForDel)) {
                                    paragraphsToRemove4.add(paragraph);
                                }
                            }
                            for (XWPFParagraph paragraph : paragraphsToRemove4) {
                                r3cell4.removeParagraph(r3cell4.getParagraphs().indexOf(paragraph));
                            }
                            List<XWPFParagraph> paragraphsToRemove5 = new ArrayList<>();
                            for (XWPFParagraph paragraph : r3cell5.getParagraphs()) {
                                String paragraphText = paragraph.getText();
                                if (paragraphText.contains("CPS"+indForDel)) {
                                    paragraphsToRemove5.add(paragraph);
                                }
                            }
                            for (XWPFParagraph paragraph : paragraphsToRemove5) {
                                r3cell5.removeParagraph(r3cell5.getParagraphs().indexOf(paragraph));
                            }
                            ArrayList<Boolean> arrayList = new ArrayList<>();
                            for (boolean value : pos2) arrayList.add(value);
                            arrayList.remove(0);
                            boolean[] newPos2 = new boolean[arrayList.size()];
                            for (int k = 0; k < newPos2.length; k++) {
                                newPos2[k] = arrayList.get(k);
                            }
                            pos2 = newPos2;
                            ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(consDocsSupp));
                            arrayList1.remove(0);
                            consDocsSupp = arrayList1.toArray(new String[0]);
                            ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(consDocsSuppUnite));
                            arrayList2.remove(0);
                            consDocsSuppUnite = arrayList2.toArray(new String[0]);
                            if (indForDel == 1) restartLoop2 = false;
                            else restartLoop2 = true;
                            indForDel--;
                            break outerloop;
                        }else {
                            restartLoop2 = false;
                        }
                    }
                }
                if (!restartLoop2) {
                    break;
                }
            }
            XWPFTableRow row5 = table.getRow(4);
            XWPFTableCell r5cell1 = row5.getCell(0);
            XWPFTableCell r5cell5 = row5.getCell(4);
    
            for (XWPFParagraph paragraph : r5cell1.getParagraphs()) {
                // Loop through all the runs in the paragraph
                for (XWPFRun run : paragraph.getRuns()) {
                    // Get the text content of the run
                    String runText = run.getText(0);
                    for (int i=1; i<=4; i++) {
                        if (runText.contains(livrables[i-1]) && pos[i-1]) {
                            for (XWPFParagraph paragraph4 : r5cell5.getParagraphs()) {
                                for (XWPFRun run4 : paragraph4.getRuns()) {
                                    String runText4 = run4.getText(0);
                                    System.out.println("----------"+runText4);
                                    if (runText4.contains("PT"+i)) {
                                        runText4 = runText4.replace("PT"+i, PTHT[i-1]);
                                        if (PTHT[i-1] != "Inclus")
                                            HT += Double.valueOf(PTHT[i-1].replaceAll("\\s", ""));
                                        run4.setText(runText4, 0);
                                    }
                                }
                            }
                        }else {
                            for (XWPFParagraph paragraph4 : r5cell5.getParagraphs()) {
                                for (XWPFRun run4 : paragraph4.getRuns()) {
                                    String runText4 = run4.getText(0);
                                    if (runText4.contains("PT"+i)&&!pos[i-1]) {
                                        runText4 = runText4.replace("PT"+i, "--");
                                        run4.setText(runText4, 0);
                                    }
                                }
                            }
                        }
                    }
    
                }
            }
    
            TVA = (HT * 20) / 100;
            PTTC = HT + TVA;
    
            XWPFTableRow row7 = table.getRow(6);
            XWPFTableCell r7cell2 = row7.getCell(1);
            XWPFTableRow row8 = table.getRow(7);
            XWPFTableCell r8cell2 = row8.getCell(1);
            XWPFTableRow row9 = table.getRow(8);
            XWPFTableCell r9cell2 = row9.getCell(1);
    
            for (XWPFParagraph paragraph : r7cell2.getParagraphs()) {
                // Loop through all the runs in the paragraph
                for (XWPFRun run : paragraph.getRuns()) {
                    // Get the text content of the run
                    String runText = run.getText(0);
                    if (runText.contains("MT")) {
                        runText = runText.replace("MT",df.format(HT).replace(',',' '));
                        run.setText(runText, 0);
                    }
                }
            }
            for (XWPFParagraph paragraph : r8cell2.getParagraphs()) {
                // Loop through all the runs in the paragraph
                for (XWPFRun run : paragraph.getRuns()) {
                    // Get the text content of the run
                    String runText = run.getText(0);
                    if (runText.contains("MV")) {
                        runText = runText.replace("MV",df.format(TVA).replace(',',' '));
                        run.setText(runText, 0);
                    }
                }
            }
            for (XWPFParagraph paragraph : r9cell2.getParagraphs()) {
                // Loop through all the runs in the paragraph
                for (XWPFRun run : paragraph.getRuns()) {
                    // Get the text content of the run
                    String runText = run.getText(0);
                    if (runText.contains("MC")) {
                        runText = runText.replace("MC",df.format(PTTC).replace(',',' '));
                        run.setText(runText, 0);
                    }
                }
            }
    
            ValueConverters converters = ValueConverters.FRENCH_INTEGER;
    
            plaaceholdersValues[plaaceholdersValues.length-1] = converters.asWords((PTTC.intValue())).toUpperCase();
            System.out.println("TESTTT!     "+converters.asWords((PTTC.intValue())).toUpperCase()+" "+plaaceholdersValues[plaaceholdersValues.length-1]);
    
            for (XWPFParagraph paragraph : paragraphs) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    for (int i = 0; i < plaaceholders.length; i++) {
                        if (text != null && text.contains(plaaceholders[i])) {
                            if (plaaceholdersValues[i]!=null) {
                                text = text.replace(plaaceholders[i], plaaceholdersValues[i]);
                                run.setText(text, 0);
                            }
                        }
                    }
                }
            }
    
            DevisSteAmiable savedDevis = deviseRepository.save(devise);
            FileOutputStream outputStream = new FileOutputStream("src/main/resources/exported_report.docx");
            document.write(outputStream);
            ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
            document.write(outputStream1);
            byte[] byteArray = outputStream1.toByteArray();
            outputStream.close();
            document.close();
            outputStream1.close();
            File file = new File("src/main/resources/exported_report.docx");
            emailService.sendHtmlMessage("anassbougaterra@gmail.com", "Demande Devis", byteArray, file);
            return savedDevis;
        }
        public List<DevisSteAmiable> findAll() {
            return deviseRepository.findAll();
        }
    
        public Optional<DevisSteAmiable> findById(Long id) {
            return deviseRepository.findById(id);
        }
    
        public int update(DevisSteAmiable devise){
            DevisSteAmiable devise1 = deviseRepository.getOne(devise.getId());
            if (devise1 == null) return -1;
            else {
                deviseRepository.save(devise);
                return 1;
            }
        }
    }

