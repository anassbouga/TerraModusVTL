package com.example.terramodusvtl.controller.devisPersonne;

import com.example.terramodusvtl.entities.devisPersonne.DevisPersonneAmiable;
import com.example.terramodusvtl.service.DevisPersonneAmiableService;
import net.sf.jasperreports.engine.JRException;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/devise-personne-amiable")
@CrossOrigin()
public class DevisPersonneAmiableController {

    @Autowired
    private DevisPersonneAmiableService devisPersonneAmiableService;


    
    @PostMapping("/")
    public DevisPersonneAmiable saveDevise(@RequestBody DevisPersonneAmiable devise) throws MessagingException, jakarta.mail.MessagingException, JRException, IOException, ParseException, XmlException {
        return devisPersonneAmiableService.saveDevise(devise);
    }

    
    @GetMapping("/")
    public List<DevisPersonneAmiable> findAll() {
        return devisPersonneAmiableService.findAll();
    }

    
    @GetMapping("/id/{id}")
    public Optional<DevisPersonneAmiable> findById(Long id) {
        return devisPersonneAmiableService.findById(id);
    }

    
    @PutMapping("/")
    public int update(@RequestBody DevisPersonneAmiable devise) {
        return devisPersonneAmiableService.update(devise);
    }
}
