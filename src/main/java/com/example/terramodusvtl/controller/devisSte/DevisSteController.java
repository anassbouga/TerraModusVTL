package com.example.terramodusvtl.controller.devisSte;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/devise-ste")
@CrossOrigin()
public class DevisSteController<T> {

    /*@Autowired
    private DevisSteRepository deviseRepository;
    @Autowired
    private TypeDevisRepository typeDevisRepository;
    @Autowired
    private DemandeurSteRepository demandeurSteRepository;
    @Autowired
    private EmailService emailService;

    @PostMapping("/")
    public void saveDevise(@RequestBody DevisSte devise) throws MessagingException, jakarta.mail.MessagingException, JRException, IOException {
        devise.setTypeDevise(typeDevisRepository.getOne(devise.getTypeDevise().getId()));
        demandeurSteRepository.save(devise.getDemandeurSte());
        devise.setDemandeurSte(demandeurSteRepository.getOne(devise.getDemandeurSte().getId()));
        deviseRepository.save(devise);
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

        String[] plaaceholders = new String[] {"ID","IValue","VILLE","DATE","OBJET","DEMANDEUR","REF","NDEVIS"};
        String ID;
        String NID;
        ID = "ICE";
        NID = devise.getDemandeurSte().getIce();

        String[] plaaceholdersValues = new String[] {ID,NID,devise.getDemandeurSte().getVille(),LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),devise.getBesoin(),devise.getDemandeurSte().getNomResponsable(),devise.getId()+"/"+LocalDate.now().getYear(),devise.getId().toString()};
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
    }

    @GetMapping("/")
    public List<DevisSte> findAll() {   return deviseRepository.findAll();
    }
    @GetMapping("/id/{id}")
    public DevisSte findById(@PathVariable Long id) {
        return deviseRepository.getOne(id);
    }
    @PutMapping("/")
    public int update(@RequestBody DevisSte devise) {
        DevisSte devise1 = deviseRepository.getOne(devise.getId());
        if (devise1 == null) return -1;
        else {
            deviseRepository.save(devise);
            return 1;
        }
    }
    */
}
