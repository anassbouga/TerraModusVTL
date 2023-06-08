package com.example.terramodusvtl.controller.devisSte;

import com.example.terramodusvtl.entities.devisSte.DevisSteAmiable;
import com.example.terramodusvtl.service.DevisSteAmiableService;
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
@RequestMapping("/api/devise-ste-amiable")
@CrossOrigin()
public class DevisSteAmiableController {

    @Autowired
    private DevisSteAmiableService devisSteAmiableService;


    
    @PostMapping("/")
    public DevisSteAmiable saveDevise(@RequestBody DevisSteAmiable devise) throws MessagingException, jakarta.mail.MessagingException, JRException, IOException, ParseException, XmlException {
        return devisSteAmiableService.saveDevise(devise);
    }

    
    @GetMapping("/")
    public List<DevisSteAmiable> findAll() {
        return devisSteAmiableService.findAll();
    }

    
    @GetMapping("/id/{id}")
    public Optional<DevisSteAmiable> findById(Long id) {
        return devisSteAmiableService.findById(id);
    }

    
    @PutMapping("/")
    public int update(@RequestBody DevisSteAmiable devise) {
        return devisSteAmiableService.update(devise);
    }
}
