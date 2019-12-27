/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.controller;

import kotikokki.domain.RaakaAine;
import kotikokki.domain.Yksikko;
import kotikokki.repository.RaakaAineRepository;
import kotikokki.repository.YksikkoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Pekka
 */
@Controller
public class DbController {
    @Autowired
    private RaakaAineRepository raRepo;
    @Autowired
    private YksikkoRepository yksRepo;
    
    @GetMapping("/tietokanta")
    public String raakaAineet(Model model){
        String otsikko = "Raaka-aineet";
        model.addAttribute("raakaAineet", raRepo.findAll());
        model.addAttribute("yksikot", yksRepo.findAll());
        model.addAttribute("otsikko", otsikko);
        return "tietokanta";
    }
   
    @PostMapping("/tietokanta/raakaAine")
    public String lisaaRaakaAine(@ModelAttribute RaakaAine raakaAine){
        raRepo.save(raakaAine);
        return "redirect:/tietokanta";
    }
    
    @PostMapping("/tietokanta/yksikko")
    public String lisaaYksikko(@ModelAttribute Yksikko yksikko){
        yksRepo.save(yksikko);
        return "redirect:/tietokanta";
    }
    
    @GetMapping("/tietokanta/yksikko/{id}/delete")
    public String poistaYksikko(@PathVariable Long id){
        yksRepo.deleteById(id);
        return "redirect:/tietokanta";
    }
    
}
