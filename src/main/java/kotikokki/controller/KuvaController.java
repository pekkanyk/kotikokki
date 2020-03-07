/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.controller;

import java.io.IOException;
import java.security.Principal;
import kotikokki.domain.Resepti;
import kotikokki.repository.ReseptiRepository;
import kotikokki.service.KuvaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Pekka
 */
@Controller
public class KuvaController {
    @Autowired
    private ReseptiRepository reseptiRepo;
    @Autowired
    private KuvaService kuvaService;
    
    @PostMapping("/resepti/{id}/kuva")
    public String lisaaKuvaReseptiin(@PathVariable Long id, @RequestParam("kuva") MultipartFile picture, Principal principal) throws IOException {
        Resepti r = reseptiRepo.getOne(id);
        if (r.getTili().getUsername().equals(principal.getName())){
            kuvaService.save(picture, r); 
        }
        
        
        return "redirect:/resepti/"+id;
    }
    
    @GetMapping("/resepti/{id}/kuva")
    public ResponseEntity<byte[]> getPicture(@PathVariable Long id){
        Resepti r = reseptiRepo.getOne(id);
        return kuvaService.haeKuva(r.getKuva().getId());
    }
    
    @GetMapping("/resepti/{id}/poistakuva")
    public String poistaKuva(@PathVariable Long id, Principal principal){
        Resepti r = reseptiRepo.getOne(id);
        if (r.getTili().getUsername().equals(principal.getName())){
            kuvaService.poistaKuvaReseptilta(id);
        }
        
        return "redirect:/resepti/"+id;
    }
}
