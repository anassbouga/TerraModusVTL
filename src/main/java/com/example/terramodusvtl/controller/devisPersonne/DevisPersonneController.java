package com.example.terramodusvtl.controller.devisPersonne;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/devise-personne")
@CrossOrigin()
public class DevisPersonneController<T> {

    /*@Autowired
    //private DevisPersonneRepository deviseRepository;
    @Autowired
    private TypeDevisRepository typeDevisRepository;
    @Autowired
    private DemandeurPersonneRepository demandeurPersonneRepository;
    @Autowired
    private EmailService emailService;


    @PostMapping("/")
    public void saveDevise(@RequestBody DevisPersonne devise) throws MessagingException, jakarta.mail.MessagingException, JRException, IOException {
        /*
        System.out.println(devise.getLat()+" "+devise.getLon()+" "+devise.getBesoin());
        devise.setTypeDevise(typeDevisRepository.getOne(devise.getTypeDevise().getId()));
        demandeurPersonneRepository.save(devise.getDemandeurPersonne());
        devise.setDemandeurPersonne(demandeurPersonneRepository.getOne(devise.getDemandeurPersonne().getId()));
        if (devise.getLat() != null && devise.getLon() != null) {
            devise.setIK(DistanceCalcService.haversine(devise.getLat(),devise.getLon()));
        }
        DevisPersonne savedDevis = deviseRepository.save(devise);
        /*
        String fileName = "C:\\Users\\anass\\Downloads\\springboot-jasperreports-advanced-demo-master\\TerraModusvc\\src\\main\\resources\\static\\demande-devis.html";
        String content= "";
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            content = new String(bytes);
            System.out.println(content);
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }  */


        /*String fileName = "C:\\Users\\anass\\Downloads\\springboot-jasperreports-advanced-demo-master\\TerraModusvc\\src\\main\\resources\\devisInfo.jrxml";
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("besoin",devise.getBesoin());
        parameters.put("typeDevise",devise.getTypeDevise().getLibelle());
        parameters.put("nom",devise.getNom());
        parameters.put("prenom",devise.getPrenom());
        parameters.put("ville",devise.getVille());
        parameters.put("phone",devise.getPhone());
        parameters.put("email",devise.getEmail());
        JasperReport report = JasperCompileManager.compileReport(fileName);
        byte[] reportBytes = JasperRunManager.runReportToPdf(report, parameters, new JREmptyDataSource());

        String[] plaaceholders = new String[] {"ID","IValue","VILLE","DATE","OBJET","DEMANDEUR","REF","NDEVIS","NOM","CIN","PHONE"};
        String ID;
        String NID;
        ID = "CIN";
        NID = devise.getDemandeurPersonne().getCin();

        String[] plaaceholdersValues = new String[] {ID,NID,devise.getDemandeurPersonne().getVille(),LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),devise.getBesoin(),devise.getDemandeurPersonne().getNom()+" "+devise.getDemandeurPersonne().getPrenom(),devise.getId()+"/"+LocalDate.now().getYear(),devise.getId().toString()};
        FileInputStream inputStream = new FileInputStream("C:\\Users\\anass\\Downloads\\springboot-jasperreports-advanced-demo-master\\TerraModusvc\\src\\main\\resources\\digital.docx");
        XWPFDocument document = new XWPFDocument(inputStream);
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String text = run.getText(0);
                System.out.println(text);
                for (int i=0;i< plaaceholders.length;i++) {
                    if (text != null && text.contains(plaaceholders[i])) {
                        text = text.replace(plaaceholders[i], plaaceholdersValues[i]);
                        run.setText(text, 0);
                    }
                }
            }
        }

        FileOutputStream outputStream = new FileOutputStream("src/main/resources/exported_report.docx");
        document.write(outputStream);
        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        document.write(outputStream1);
        byte[] byteArray = outputStream1.toByteArray();
        outputStream.close();
        document.close();
        outputStream1.close();
        File file = new File("C:\\Users\\anass\\Downloads\\exported_report.docx");
        emailService.sendHtmlMessage("ouadi3laila@gmail.com","Demande Devis",byteArray,file);
        return savedDevis;

    }

    @GetMapping("/")
    public List<DevisPersonne> findAll() {  return deviseRepository.findAll();
    }
    @GetMapping("/id/{id}")
    public DevisPersonne findById(@PathVariable Long id) {
        return deviseRepository.getOne(id);
    }
    @PutMapping("/")
    public int update(@RequestBody DevisPersonne devise) {
        DevisPersonne devise1 = deviseRepository.getOne(devise.getId());
        if (devise1 == null) return -1;
        else {
            deviseRepository.save(devise);
            return 1;
        }
    }

    }

         */
}