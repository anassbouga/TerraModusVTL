package com.example.terramodusvtl.controller.devisPersonne;

import com.example.terramodusvtl.entities.devisPersonne.DevisPersonneOpci;
import com.example.terramodusvtl.service.DevisPersonneOpciService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/devise-personne-opci")
@CrossOrigin()
public class DevisPersonneOpciController {

    @Autowired
    private DevisPersonneOpciService devisPersonneOpciService;


    
    @PostMapping("/")
    public DevisPersonneOpci saveDevise(@RequestBody DevisPersonneOpci devise) throws MessagingException, jakarta.mail.MessagingException, JRException, IOException {
        return devisPersonneOpciService.saveDevise(devise);
    }

    
    @GetMapping("/")
    public List<DevisPersonneOpci> findAll() {
        return devisPersonneOpciService.findAll();
    }

    
    @GetMapping("/id/{id}")
    public Optional<DevisPersonneOpci> findById(Long id) {
        return devisPersonneOpciService.findById(id);
    }

    
    @PutMapping("/")
    public int update(@RequestBody DevisPersonneOpci devise) {
        return devisPersonneOpciService.update(devise);
    }
}
