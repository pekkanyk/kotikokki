/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.controller;

import kotikokki.service.TiliService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Pekka
 */
@Controller
public class TiliController {
    @Autowired
    private TiliService tiliService;
    
    @GetMapping("/rekisteroidy")
    public String rekisteroidy(){
        return "rekisteroidy";
    }
    
    @PostMapping("/rekisteroidy")
    public String lisaaTili(@RequestParam String username, 
                            @RequestParam String password, 
                            @RequestParam String password2, 
                            @RequestParam String kokoNimi,
                            Model model) {
        model.addAttribute("error", tiliService.luoTili(username, password, password2, kokoNimi));
        model.addAttribute("uusi", true);
        return "index";
    }
    
}
