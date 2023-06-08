package com.example.terramodusvtl.controller.devisSte;

import com.example.terramodusvtl.entities.devisSte.DevisSteOpci;
import com.example.terramodusvtl.service.DevisSteOpciService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/devise-ste-opci")
@CrossOrigin()
public class DevisSteOpciController {

    @Autowired
    private DevisSteOpciService devisSteOpciService;


    
    @PostMapping("/")
    public DevisSteOpci saveDevise(@RequestBody DevisSteOpci devise) throws MessagingException, jakarta.mail.MessagingException, JRException, IOException {
        return devisSteOpciService.saveDevise(devise);
    }

    
    @GetMapping("/")
    public List<DevisSteOpci> findAll() {
        return devisSteOpciService.findAll();
    }

    
    @GetMapping("/id/{id}")
    public Optional<DevisSteOpci> findById(Long id) {
        return devisSteOpciService.findById(id);
    }

    
    @PutMapping("/")
    public int update(@RequestBody DevisSteOpci devise) {
        return devisSteOpciService.update(devise);
    }
}
