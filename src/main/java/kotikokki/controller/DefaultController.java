/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    public String listaus(Model model, @RequestParam(required = false) String value, @RequestParam(required = false) String nappi, @RequestParam(required = false) String date){
        if (nappi==null) nappi="main";
        model.addAttribute("totalRows",vkTuoteService.rivienLkm("deleted"));
        
        if (nappi.equals("Poistunut")){
            if(!date.isEmpty()){
                LocalDate date_parsed = LocalDate.parse(date);
                model.addAttribute("riveja",vkTuoteService.historiaHakuTarkkaPaiva(date_parsed).size());
                model.addAttribute("outletHistoriaTuotteet",vkTuoteService.historiaHakuTarkkaPaiva(date_parsed));
            }
            
            else {
                if (!isNumeric(value)){
                    int riveja = vkTuoteService.historiaLista("date").size();
                    model.addAttribute("riveja",riveja);
                    model.addAttribute("outletHistoriaTuotteet",vkTuoteService.historiaLista("date"));
                }
                else {
                    model.addAttribute("riveja",vkTuoteService.historiahaku("date", value).size());
                    model.addAttribute("outletHistoriaTuotteet",vkTuoteService.historiahaku("date", value));
                }
                }
        }
        else if (nappi.equals("Tuote")){
            value = "%"+value+"%";
            model.addAttribute("outletHistoriaTuotteet", vkTuoteService.historiahaku("tuote", value));
            int riveja = vkTuoteService.historiahaku("tuote", value).size();
            model.addAttribute("riveja",riveja);
        }
        else {
            model.addAttribute("riveja",vkTuoteService.historiaHakuTarkkaPaiva(LocalDate.now()).size());
            model.addAttribute("outletHistoriaTuotteet",vkTuoteService.historiaHakuTarkkaPaiva(LocalDate.now()));
        }
        
        return "outletHistoria";
    }
    @GetMapping("/vkoutlet")
    public String outletSort(Model model, @RequestParam(required = false) String value, @RequestParam(required = false) String nappi, @RequestParam(required = false) String date){
        model.addAttribute("totalRows",vkTuoteService.rivienLkm("active"));
        if (nappi == null) {
            nappi="Ale";
            value="10";
        }
        
        if (nappi.equals("Ale")){
            if (!isNumeric(value)) value="100";
            model.addAttribute("outletTuotteet", vkTuoteService.alePros(Double.valueOf(value),"asc"));
            int riveja = vkTuoteService.alePros(Double.valueOf(value),"asc").size();
            model.addAttribute("riveja",riveja);
            
        }
        
        else if (nappi.equals("Aled")){
            if (!isNumeric(value)) value="100";
            model.addAttribute("outletTuotteet", vkTuoteService.alePros(Double.valueOf(value),"des"));
            int riveja = vkTuoteService.alePros(Double.valueOf(value),"des").size();
            model.addAttribute("riveja",riveja);
        }
        
        
        else if (nappi.equals("IDa")){
            model.addAttribute("outletTuotteet", vkTuoteService.listDbByOutId("asc"));
            int riveja = vkTuoteService.listDbByOutId("asc").size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("IDd")){
            model.addAttribute("outletTuotteet", vkTuoteService.listDbByOutId("des"));
            int riveja = vkTuoteService.listDbByOutId("des").size();
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
            int riveja = vkTuoteService.listByHinta(Double.valueOf(value),"asc").size();
            model.addAttribute("riveja",riveja);
        }
        else if (nappi.equals("Hintad")){
            if (!isNumeric(value)) value="99999";
            model.addAttribute("outletTuotteet", vkTuoteService.listByHinta(Double.valueOf(value),"des"));
            int riveja = vkTuoteService.listByHinta(Double.valueOf(value),"des").size();
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
            if (!date.isEmpty()){
                value = String.valueOf(ChronoUnit.DAYS.between(LocalDate.parse(date), LocalDate.now())); 
            }
            
            if (value==""){
                value="0";
            }
            else if (!isNumeric(value)) value="0";
            
            //LocalDate pvSitten = LocalDate.now();
            //pvSitten=pvSitten.minusDays(Long.valueOf(value));
            
            model.addAttribute("outletTuotteet", vkTuoteService.listByMuuttunut(value));
            int riveja = vkTuoteService.listByMuuttunut(value).size();
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
