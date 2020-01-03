/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kotikokki.domain.ReseptiRaakaAine;
import kotikokki.domain.Resepti;
import kotikokki.domain.Tili;
import kotikokki.repository.ReseptiRaakaAineRepository;
import kotikokki.repository.ReseptiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Pekka
 */
@Service
public class ReseptiService {
    
    @Autowired
    private ReseptiRepository reseptiRepo;
    @Autowired
    private ReseptiRaakaAineRepository reseptiRaakaAineRepo;
    @Autowired
    private RaakaAineService raakaAineService;
    @Autowired
    private TiliService tiliService;
    
    public void lisaaUusi(String nimi, List<String> valinnat, List<String> raakaAineet, List<Double> maarat, List<String> yksikot, String ohje, Long tiliId){
        Resepti resepti = new Resepti();
        resepti.setNimi(nimi);
        if (valinnat.contains("julkinen")) resepti.setJulkinen(true);
        resepti.setOhje(ohje);
        resepti.setLisatty(LocalDateTime.now());
        resepti.setTili(tiliService.haeTiliIdlla(tiliId));
        Resepti r = reseptiRepo.save(resepti);
        
        for (int i=0; i<raakaAineet.size();i++){
            ReseptiRaakaAine rra = new ReseptiRaakaAine();
            rra.setRaakaAine(raakaAineService.lisaaJosEiOlemassa(raakaAineet.get(i)));
            rra.setMaara(maarat.get(i));
            rra.setYksikko(yksikot.get(i));
            rra.setResepti(r);
            reseptiRaakaAineRepo.save(rra);
        }
        //return r;
    }
    
    public void editoiResepti(Long id, String nimi, List<String> valinnat, List<String> raakaAineet, List<Double> maarat, List<String> yksikot, String ohje){
        Resepti resepti = reseptiRepo.getOne(id);
        List<ReseptiRaakaAine> vanhat = reseptiRaakaAineRepo.findByResepti(resepti);
        List<ReseptiRaakaAine> uudet = new ArrayList<>();
        List<ReseptiRaakaAine> poistettavat = new ArrayList<>();
        List<ReseptiRaakaAine> lisattavat = new ArrayList<>();
        resepti.setJulkinen(false);
        if (valinnat.contains("julkinen")) resepti.setJulkinen(true);
        
        resepti.setNimi(nimi);
        resepti.setOhje(ohje);
        reseptiRepo.save(resepti);
        for (int i=0; i<raakaAineet.size();i++){
            ReseptiRaakaAine rra = new ReseptiRaakaAine();
            rra.setRaakaAine(raakaAineService.lisaaJosEiOlemassa(raakaAineet.get(i)));
            rra.setMaara(maarat.get(i));
            rra.setYksikko(yksikot.get(i));
            rra.setResepti(resepti);
            uudet.add(rra);
        }
        
        for (ReseptiRaakaAine rra : uudet) {
            if (!vanhat.contains(rra)) lisattavat.add(rra);
        }
        
        for (ReseptiRaakaAine rra : vanhat) {
            if (!uudet.contains(rra)) poistettavat.add(rra);
        }
        
        for (ReseptiRaakaAine rra : lisattavat){
            reseptiRaakaAineRepo.save(rra);
        }
        
        for (ReseptiRaakaAine rra : poistettavat){
            reseptiRaakaAineRepo.delete(rra);
        }
        
    }
    
    public List<Resepti> haeKaikki (){
        return reseptiRepo.findAll();
    }
    
    public Resepti haeResepti(Long id){
        return reseptiRepo.getOne(id);
    }
    
    @Transactional
    public void poistaResepti(Long reseptiId){
        Resepti resepti = reseptiRepo.getOne(reseptiId);
        reseptiRaakaAineRepo.deleteByResepti(resepti);
        reseptiRepo.delete(resepti);
        
    }
    
    public List<Resepti> haeJulkiset(){
        return reseptiRepo.findByJulkinen(true);
    }
    
    public List<Resepti> haeKayttajanReseptit(Tili tili){
        return reseptiRepo.findByTili(tili);
    }
    
    public List<Resepti> haeTilinJaJulkiset(Tili tili){
        return reseptiRepo.findByTiliOrJulkinen(tili, true);
    }
    
    public Long laskeReseptit(){
        return reseptiRepo.count();
    }
    
    
}
