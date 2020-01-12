/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.controller;

import java.security.Principal;
import kotikokki.domain.Optio;
import kotikokki.domain.RaakaAine;
import kotikokki.domain.Yksikko;
import kotikokki.repository.OptioRepository;
import kotikokki.repository.RaakaAineRepository;
import kotikokki.service.YksikkoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Pekka
 */
@Controller
public class DbController {
    @Autowired
    private RaakaAineRepository raRepo;
    @Autowired
    private YksikkoService yksikkoService;
    @Autowired
    private OptioRepository optioRepo;
    
    @GetMapping("/tietokanta")
    public String raakaAineet(Model model, Principal principal){
        String otsikko = "Tietokanta-admin";
        model.addAttribute("kirjautunut", "true");
        model.addAttribute("raakaAineet", raRepo.findAll());
        model.addAttribute("yksikot", yksikkoService.findAll());
        model.addAttribute("otsikko", otsikko);
        return "tietokanta";
    }
    
    @GetMapping("/tietokanta/yksikot")
    public String yksikot(Model model){
        model.addAttribute("kirjautunut", "true");
        model.addAttribute("otsikko", "Yksikot");
        model.addAttribute("yksikot", yksikkoService.findAll());
        return "yksikot";
    }
  
    @PostMapping("/tietokanta/yksikko")
    public String lisaaYksikko(@ModelAttribute Yksikko yksikko){
        yksikkoService.tallenna(yksikko);
        return "redirect:/tietokanta/yksikot";
    }
    
    @GetMapping("/tietokanta/yksikko/{id}/delete")
    public String poistaYksikko(@PathVariable Long id){
        yksikkoService.poista(id);
        return "redirect:/tietokanta/yksikot";
    }
    
    @PostMapping("/tietokanta/raakaAineet/lisaa")
    public String lisaaRaakaAine(@ModelAttribute RaakaAine raakaAine){
        raRepo.save(raakaAine);
        return "redirect:/tietokanta/raakaAineet";
    }
    
    @GetMapping("/tietokanta/raakaAineet")
    public String raakaAineet(Model model){
        model.addAttribute("kirjautunut", "true");
        model.addAttribute("otsikko", "Raaka-aineet");
        model.addAttribute("optiot", optioRepo.findByValikkoOrderByNimi("ra_osasto"));
        model.addAttribute("raakaAineet", raRepo.findAll());
        return "raakaAineet";
    }
    
    @GetMapping("/tietokanta/optiot")
    public String optiot(Model model){
        model.addAttribute("kirjautunut", "true");
        model.addAttribute("otsikko", "Optiot");
        model.addAttribute("optiot", optioRepo.findAll());
        return "optiot";
    }
    
    @PostMapping("/tietokanta/optiot/lisaa")
    public String lisaaOptio(@ModelAttribute Optio optioOlio){
        optioRepo.save(optioOlio);
        return "redirect:/tietokanta/optiot";
    }
    
    @GetMapping("/tietokanta/optiot/{id}/delete")
    public String poistaOptio(@PathVariable Long id){
        optioRepo.deleteById(id);
        return "redirect:/tietokanta/optiot";
    }
}
