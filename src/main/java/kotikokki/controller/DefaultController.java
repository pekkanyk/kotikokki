/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import kotikokki.service.ListaService;
import kotikokki.service.ReseptiService;
import kotikokki.service.TiliService;
import kotikokki.service.VkTuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;




/**
 *
 * @author Pekka
 */
@Controller
public class DefaultController {
    
    @Autowired
    private ReseptiService reseptiService;
    @Autowired
    private TiliService tiliService;
    @Autowired
    private ListaService listaService;
    @Autowired
    private VkTuoteService vkTuoteService;
    
    /*
    @GetMapping("*")
    public String eiLoydy() {
        return "redirect:/";
    }
    */
    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal!=null) model.addAttribute("kirjautunut", "true");
        else model.addAttribute("kirjautunut", "false");
        model.addAttribute("reseptiLkm", reseptiService.laskeReseptit());
        model.addAttribute("reseptitKuvalla", reseptiService.laskeKuvalliset());
        model.addAttribute("julkisetLkm", reseptiService.laskeJulkiset());
        model.addAttribute("kayttajaLkm", tiliService.laskeKayttajat());
        return "index";
    }
    
    @GetMapping("/listat")
    public String listat(Model model) {
        return "listat";
    }
    @PostMapping("/listat")
    public String generoiListat(Model model, @RequestParam String lista_cp){
        
        model.addAttribute("nro1", listaService.sivut(lista_cp));
        return "listat_generate";
    }
    
    @GetMapping("/vkoutlet/reload")
    public String lataaLista() throws IOException {
        vkTuoteService.reloadDb();
        return "redirect:/vkoutlet";
    }
    @GetMapping("/vkoutlet/historia")
    public String listaus(Model model, @RequestParam(required = false) String value, @RequestParam(required = false) String nappi){
        if (nappi==null) nappi="main";
        if (nappi.equals("Poistunut")){
            if (!isNumeric(value)) value = "0";
            LocalDate hakupv = LocalDate.now().minusDays(Long.parseLong(value));
            int riveja = vkTuoteService.historiahaku("date", hakupv.toString()).size();
            model.addAttribute("riveja",riveja);
            model.addAttribute("outletHistoriaTuotteet",vkTuoteService.historiahaku("date", hakupv.toString()));
            }
        else if (nappi.equals("Tuote")){
            value = "%"+value+"%";
            model.addAttribute("outletHistoriaTuotteet", vkTuoteService.historiahaku("tuote", value));
            int riveja = vkTuoteService.historiahaku("tuote", value).size();
            model.addAttribute("riveja",riveja);
        }
        else {
            int riveja = vkTuoteService.historiaLista("").size();
            model.addAttribute("riveja",riveja);
            model.addAttribute("outletHistoriaTuotteet",vkTuoteService.historiaLista("pid"));
        }
        
        return "outletHistoria";
    }
    @GetMapping("/vkoutlet")
    public String outletSort(Model model, @RequestParam(required = false) String value, @RequestParam(required = false) String nappi, @RequestParam(required = false) String extra){
        //if (value == null) value="";
        if (nappi == null) nappi="Ale";
        
        if (extra!=null){
                if (!isNumeric(value)||value ==null) value="100";
                model.addAttribute("outletTuotteet", vkTuoteService.listaaKaikkiAlleAlePaitsi(Double.valueOf(value)+0.01,extra));
                int riveja = vkTuoteService.listaaKaikkiAlleAlePaitsi(Double.valueOf(value)+0.01,extra).size();
                model.addAttribute("riveja",riveja);
            }
        
        else if (nappi.equals("Ale")){
            if (!isNumeric(value)||value ==null) value="100";
            model.addAttribute("outletTuotteet", vkTuoteService.alePros(Double.valueOf(value)+0.01,""));
            int riveja = vkTuoteService.alePros(Double.valueOf(value),"").size();
            model.addAttribute("riveja",riveja);
        }
        
        else if (nappi.equals("Aled")){
            if (!isNumeric(value)) value="100";
            model.addAttribute("outletTuotteet", vkTuoteService.alePros(Double.valueOf(value)+0.01,"des"));
            int riveja = vkTuoteService.alePros(Double.valueOf(value),"").size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("IDa")){
            model.addAttribute("outletTuotteet", vkTuoteService.listDbByOutId("asc"));
            int riveja = vkTuoteService.listDbByOutId("").size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("IDd")){
            model.addAttribute("outletTuotteet", vkTuoteService.listDbByOutId("des"));
            int riveja = vkTuoteService.listDbByOutId("").size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("Tuote")){
            value = "%"+value+"%";
            model.addAttribute("outletTuotteet", vkTuoteService.listByNimi(value));
            int riveja = vkTuoteService.listByNimi(value).size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("Hinta")){
            if (!isNumeric(value)) value="99999";
            model.addAttribute("outletTuotteet", vkTuoteService.listByHinta(Double.valueOf(value),"asc"));
            int riveja = vkTuoteService.listByHinta(Double.valueOf(value),"").size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("Hintad")){
            if (!isNumeric(value)) value="99999";
            model.addAttribute("outletTuotteet", vkTuoteService.listByHinta(Double.valueOf(value),""));
            int riveja = vkTuoteService.listByHinta(Double.valueOf(value),"").size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("A")){
            model.addAttribute("outletTuotteet", vkTuoteService.listByPoisto(false));
            int riveja = vkTuoteService.listByPoisto(false).size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("D")){
            model.addAttribute("outletTuotteet", vkTuoteService.listByDumppi(true));
            int riveja = vkTuoteService.listByDumppi(true).size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("Muuttunut")){
            if (value==""){
                value="0";
            }
            else if (!isNumeric(value)) value="0";
            
            LocalDate pvSitten = LocalDate.now();
            pvSitten=pvSitten.minusDays(Long.valueOf(value));
            
            model.addAttribute("outletTuotteet", vkTuoteService.listByMuuttunut(pvSitten));
            int riveja = vkTuoteService.listByMuuttunut(pvSitten).size();
            model.addAttribute("riveja",riveja);
        }
        else {
     
            int riveja = vkTuoteService.alePros(4.4,"").size();
            model.addAttribute("riveja",riveja);
            //model.addAttribute("tuotteita",tuotteita);
            //model.addAttribute("outletTuotteet",vkTuoteService.listDb());
            model.addAttribute("outletTuotteet",vkTuoteService.alePros(4.4,""));
     
        }
        return "outlet";
    }
    
    /*
    @PostMapping("/vkoutlet")
    public String outletSort(Model model, @RequestParam String value, @RequestParam String nappi){
        if (nappi.equals("Ale")){
            if (!isNumeric(value)) value="100";
            model.addAttribute("outletTuotteet", vkTuoteService.alePros(Double.valueOf(value)+0.01,""));
            int riveja = vkTuoteService.alePros(Double.valueOf(value),"").size();
            model.addAttribute("riveja",riveja);
        }
        if (nappi.equals("Aled")){
            if (!isNumeric(value)) value="100";
            model.addAttribute("outletTuotteet", vkTuoteService.alePros(Double.valueOf(value)+0.01,"des"));
            int riveja = vkTuoteService.alePros(Double.valueOf(value),"").size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("IDa")){
            model.addAttribute("outletTuotteet", vkTuoteService.listDbByOutId("asc"));
            int riveja = vkTuoteService.listDbByOutId("").size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("IDd")){
            model.addAttribute("outletTuotteet", vkTuoteService.listDbByOutId("des"));
            int riveja = vkTuoteService.listDbByOutId("").size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("Tuote")){
            value = "%"+value+"%";
            model.addAttribute("outletTuotteet", vkTuoteService.listByNimi(value));
            int riveja = vkTuoteService.listByNimi(value).size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("Hinta")){
            if (!isNumeric(value)) value="99999";
            model.addAttribute("outletTuotteet", vkTuoteService.listByHinta(Double.valueOf(value),"asc"));
            int riveja = vkTuoteService.listByHinta(Double.valueOf(value),"").size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("Hintad")){
            if (!isNumeric(value)) value="99999";
            model.addAttribute("outletTuotteet", vkTuoteService.listByHinta(Double.valueOf(value),""));
            int riveja = vkTuoteService.listByHinta(Double.valueOf(value),"").size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("A")){
            model.addAttribute("outletTuotteet", vkTuoteService.listByPoisto(false));
            int riveja = vkTuoteService.listByPoisto(false).size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("D")){
            model.addAttribute("outletTuotteet", vkTuoteService.listByDumppi(true));
            int riveja = vkTuoteService.listByDumppi(true).size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("Muuttunut")){
            if (value==""){
                value="0";
            }
            else if (!isNumeric(value)) value="14";
            
            LocalDate pvSitten = LocalDate.now();
            pvSitten=pvSitten.minusDays(Long.valueOf(value)-1);
            
            model.addAttribute("outletTuotteet", vkTuoteService.listByMuuttunut(pvSitten));
            int riveja = vkTuoteService.listByMuuttunut(pvSitten).size();
            model.addAttribute("riveja",riveja);
        }
        
        return "outlet";
    }
    */
    
    /*
    @GetMapping("/vkoutlet/remove")
    public String tyhjennaLista() throws IOException {
        vkTuoteService.delDb();
        return "redirect:/vkoutlet";
    }
    */
    private static boolean isNumeric(String strNum) {
    if (strNum == null) {
        return false;
    }
    try {
        double d = Double.parseDouble(strNum);
    } catch (NumberFormatException nfe) {
        return false;
    }
    return true;
}
    
}
