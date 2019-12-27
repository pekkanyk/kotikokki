/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.controller;

import java.security.Principal;
import java.util.List;
import kotikokki.domain.Resepti;
import kotikokki.domain.Tili;
import kotikokki.repository.YksikkoRepository;
import kotikokki.service.ReseptiService;
import kotikokki.service.TiliService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Pekka
 */
@Controller
public class ReseptiController {
    @Autowired
    private ReseptiService reseptiService;
    @Autowired
    private YksikkoRepository yksikkoRepo;
    @Autowired
    private TiliService tiliService;
    
        
    @GetMapping("/reseptit")
    private String reseptit(Model model, Principal principal){
        model.addAttribute("otsikko", "Reseptit");
        
        if (principal == null){
            model.addAttribute("reseptit", reseptiService.haeJulkiset());
            model.addAttribute("kirjautunut", "false");
        }
        else{
            Tili tili = tiliService.haeTiliKayttajanimella(principal.getName());
            //model.addAttribute("reseptit", reseptiService.haeKayttajanReseptit(tili));
            model.addAttribute("username", principal.getName());
            model.addAttribute("reseptit", reseptiService.haeTilinJaJulkiset(tili));
            model.addAttribute("kirjautunut", "true");
        } 
        
        return "reseptit";
    }
    
    @GetMapping("/reseptit/lisaa")
    private String uusiResepti(Model model, Principal principal){
        model.addAttribute("otsikko", "Uusi reseptit");
        model.addAttribute("kirjautunut", "true");
        model.addAttribute("tili", tiliService.haeTiliKayttajanimella(principal.getName()));
        model.addAttribute("yksikot", yksikkoRepo.findAllByOrderByNimiAsc());
        return "lisaa";
    }
    
    @PostMapping("/reseptit/lisaa")
    public String lisaaResepti(@RequestParam String nimi, 
                                @RequestParam("raakaAine") List<String> raakaAineet,
                                @RequestParam("maara") List<Double> maarat,
                                @RequestParam("yksikko") List<String> yksikot,
                                @RequestParam String ohje,
                                @RequestParam Long tiliId){
        reseptiService.lisaaUusi(nimi, raakaAineet, maarat, yksikot, ohje, tiliId);
        return "redirect:/reseptit";
    }
    
    @GetMapping("/resepti/{id}")
    private String reseptit(@PathVariable Long id, Model model, Principal principal){
        if (principal == null){
            model.addAttribute("resepti", reseptiService.haeResepti(id));
            model.addAttribute("kirjautunut", "false");
        }
        else{
            model.addAttribute("username", principal.getName());
            model.addAttribute("resepti", reseptiService.haeResepti(id));
            model.addAttribute("kirjautunut", "true");
        } 
        return "resepti";
    }
    
    @GetMapping("/resepti/{id}/editoi")
    private String reseptinMuokkaus(@PathVariable Long id, Model model, Principal principal){
        Resepti r = reseptiService.haeResepti(id);
        if (r.getTili().getUsername().equals(principal.getName())){
            model.addAttribute("resepti",r);
            model.addAttribute("yksikot", yksikkoRepo.findAllByOrderByNimiAsc());
            return "editoi";
        }
        else return "redirect:/resepti/"+id;
        
    }
    
    @PostMapping("/reseptit/{id}/tallenna")
    public String editoiResepti(@PathVariable Long id,
                                @RequestParam String nimi, 
                                @RequestParam("raakaAine") List<String> raakaAineet,
                                @RequestParam("maara") List<Double> maarat,
                                @RequestParam("yksikko") List<String> yksikot,
                                @RequestParam String ohje,
                                Principal principal){
        Resepti r = reseptiService.haeResepti(id);
        if (r.getTili().getUsername().equals(principal.getName())){
            reseptiService.editoiResepti(id, nimi, raakaAineet, maarat, yksikot, ohje);
            return "redirect:/resepti/"+id;
        }
        else return "redirect:/resepti/"+id;
    }
    
    @GetMapping("/resepti/{id}/poista")
    private String poistaResepti(@PathVariable Long id, Principal principal){
        Resepti r = reseptiService.haeResepti(id);
        if (r.getTili().getUsername().equals(principal.getName())){
            reseptiService.poistaResepti(id);
            return "redirect:/reseptit";
        }
        else return "redirect:/reseptit";
    }
}
