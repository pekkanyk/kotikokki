/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.controller;

import java.security.Principal;
import kotikokki.domain.Tili;
import kotikokki.service.TiliService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Pekka
 */
@Controller
public class RuokalistaController {
    @Autowired
    private TiliService tiliService;
    
    
    @GetMapping("/ruokalista")
    public String haeRuokalista(Principal principal){
        Tili tili = tiliService.haeTiliKayttajanimella(principal.getName());
        //TODO
        return "redirect:/";
    }
    
}
