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
import java.util.ArrayList;
import java.util.List;
import kotikokki.domain.OutletTuote;
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
    
    @GetMapping("/vkoutlet/piilossa")
    public String piilotetut(Model model) {
        return "piilossa";
    }
    @PostMapping("/vkoutlet/piilossa")
    public String haePiilotetut(Model model, @RequestParam String lista_cp){
        
        model.addAttribute("loytyneet", listaService.etsiPiilotetut(lista_cp));
        return "loydetyt";
    }
    
    @GetMapping("/vkoutlet/reload")
    public String lataaLista() throws IOException {
        vkTuoteService.reloadDb();
        return "redirect:/vkoutlet";
    }
    
    @GetMapping("/vkoutlet/stats")
    public String statistiikka(Model model){
        model.addAttribute("listaHaettu",vkTuoteService.listaHaettuAika());
        model.addAttribute("totalRows_a",vkTuoteService.rivienLkm("active"));
        model.addAttribute("totalRows_d",vkTuoteService.rivienLkm("deleted"));
        model.addAttribute("active_distinct",vkTuoteService.aktiivisetEriTuotteet());
        model.addAttribute("active_paivat", vkTuoteService.aktiivisetPvm());
        model.addAttribute("deleted_paivat", vkTuoteService.deletedPvm());
        model.addAttribute("active_ka", vkTuoteService.activeKa());
        model.addAttribute("deleted_ka", vkTuoteService.deletedKa());
        model.addAttribute("lisatyt", vkTuoteService.lisatytPvm());
        model.addAttribute("poistumiskeskiarvo", vkTuoteService.lisatytKeskimaarinAktiivisena());
        model.addAttribute("numerot", vkTuoteService.countLastNbrs());
        model.addAttribute("activeKa", vkTuoteService.keskimaarinAktiivisena());
        model.addAttribute("deletedKuntoluokatPros", vkTuoteService.deletedProssatKuntoluokilla());
        model.addAttribute("today",LocalDate.now().toString());
        return "stats";
    }
    
    @GetMapping("/vkoutlet/historia")
    public String listaus(Model model, @RequestParam(required = false) String value, @RequestParam(required = false) String nappi, @RequestParam(required = false) String date, @RequestParam(required = false) String alkaen){
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
            if (isNumeric(value)){
                //model.addAttribute("outletHistoriaTuotteet",vkTuoteService.haePidMukaan("deleted", Integer.valueOf(value)));
                //model.addAttribute("riveja",vkTuoteService.haePidMukaan("deleted", Integer.valueOf(value)).size());
            }
            else {
            value = "%"+value+"%";
            if (alkaen.equals("")) alkaen = LocalDate.now().toString();
            if (date.equals("")) date = "2020-12-01";
            List<OutletTuote> outletit = vkTuoteService.historiaAlkaenLoppuu(value, date, alkaen);
            model.addAttribute("alennus",vkTuoteService.keskiArvoAlennus(value, date, alkaen));
            model.addAttribute("outletHistoriaTuotteet", outletit);
            model.addAttribute("riveja",outletit.size());
            /*
            model.addAttribute("outletHistoriaTuotteet", vkTuoteService.historiahaku("tuote", value));
            int riveja = vkTuoteService.historiahaku("tuote", value).size();
            model.addAttribute("riveja",riveja);
            */
            }
        }
        else {
            model.addAttribute("riveja",vkTuoteService.historiaHakuTarkkaPaiva(LocalDate.now()).size());
            model.addAttribute("outletHistoriaTuotteet",vkTuoteService.historiaHakuTarkkaPaiva(LocalDate.now()));
        }
        
        return "outletHistoria";
    }
    @GetMapping("/vkoutlet")
    public String outletSort(Model model, @RequestParam(required = false) String value, 
                                          @RequestParam(required = false) String nappi, 
                                          @RequestParam(required = false) String date,
                                          @RequestParam(required = false) String alkaen,
                                          @RequestParam(required = false) String activity,
                                          @RequestParam(required = false) String sort    ){
        model.addAttribute("hakusana",value);
        model.addAttribute("date",date);
        model.addAttribute("alkaen",alkaen);
        model.addAttribute("activity",activity);
        model.addAttribute("sort",sort);
        model.addAttribute("totalRows",vkTuoteService.rivienLkm("active"));
        model.addAttribute("listaHaettu",vkTuoteService.listaHaettuAika());
        model.addAttribute("twoWeeksAgo",LocalDate.now().minusDays(14).toString());
        model.addAttribute("monthAgo",LocalDate.now().minusDays(32).toString());
        model.addAttribute("today",LocalDate.now().toString());
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
        
        else if (nappi.equals("haku")){
            List<OutletTuote> outletTuotteet = new ArrayList();
            if (isNumeric(value)){
                outletTuotteet = vkTuoteService.haePidMukaan(Integer.valueOf(value),activity);
            }
            else {
                outletTuotteet = vkTuoteService.haeNimellaAlkaenAsti(value, activity, sort, alkaen, date);
                model.addAttribute("alennus",vkTuoteService.keskiArvoAlennusAlkaenAsti(value, activity, alkaen, date));
                model.addAttribute("aletKuntoluokittain",vkTuoteService.aletkuntoluokittain(value, activity, alkaen, date));
                model.addAttribute("sumOutPrice",vkTuoteService.outPriceSumma(value, activity, alkaen, date));
            }
            model.addAttribute("outletTuotteet", outletTuotteet);
            model.addAttribute("riveja",outletTuotteet.size());
            
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
            if (isNumeric(value)){
                //model.addAttribute("outletTuotteet", vkTuoteService.haePidMukaan("active", Integer.valueOf(value)));
                //model.addAttribute("riveja",vkTuoteService.haePidMukaan("active", Integer.valueOf(value)).size());
            }
            else {
            value = "%"+value+"%";
            model.addAttribute("outletTuotteet", vkTuoteService.listByNimi(value));
            int riveja = vkTuoteService.listByNimi(value).size();
            model.addAttribute("riveja",riveja);
                    }
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
            if (isNumeric(value)){
                List<OutletTuote> outletit = vkTuoteService.yliPaivaaSittenMyyty(value);
                model.addAttribute("outletTuotteet",outletit);
                model.addAttribute("riveja",outletit.size());
                
            }
            else {
            model.addAttribute("outletTuotteet", vkTuoteService.listByPoisto(false));
            int riveja = vkTuoteService.listByPoisto(false).size();
            model.addAttribute("riveja",riveja);
            }
        }
        else if (nappi.equals("D")){
            if (isNumeric(value)){
                //model.addAttribute("outletTuotteet", vkTuoteService.yliAikaUseampiTuote(Integer.parseInt(value)));
                //model.addAttribute("riveja",vkTuoteService.yliAikaUseampiTuote(Integer.parseInt(value)).size());
                if (date.isEmpty()) date = LocalDate.now().toString();
                List<OutletTuote> lista = vkTuoteService.yliXaktiivista(Integer.parseInt(value),date);
                
                model.addAttribute("outletTuotteet", lista);
                model.addAttribute("riveja",lista.size());
            }
            else if (value.equals("v")){
                List<OutletTuote> lista = vkTuoteService.dumppiVarastolla();
                model.addAttribute("outletTuotteet", lista);
                model.addAttribute("riveja",lista.size());
            }
            else {
                model.addAttribute("outletTuotteet", vkTuoteService.listByDumppi(true));
                model.addAttribute("riveja",vkTuoteService.listByDumppi(true).size());
            }
            
        }
        else if (nappi.equals("Muuttunut")){
            if (!date.isEmpty() && isNumeric(value)){
                if (value.equals("-10")){
                    model.addAttribute("outletTuotteet", vkTuoteService.aktiivisiaPvm(LocalDate.parse(date)));
                    model.addAttribute("riveja", vkTuoteService.aktiivisiaPvm(LocalDate.parse(date)).size());
                }
                else if (value.equals("-11")){
                    model.addAttribute("outletTuotteet", vkTuoteService.firstSeenPvm(LocalDate.parse(date)));
                    model.addAttribute("riveja", vkTuoteService.firstSeenPvm(LocalDate.parse(date)).size());
                }
                else{
                    model.addAttribute("outletTuotteet", vkTuoteService.ennenPvYliHinnan(LocalDate.parse(date), Double.valueOf(value)));
                    model.addAttribute("riveja",vkTuoteService.ennenPvYliHinnan(LocalDate.parse(date), Double.valueOf(value)).size());
                }
            }
            else {
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
    if (strNum.matches(".*[a-z].*") || strNum.matches(".*[A-Z].*")) { 
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
