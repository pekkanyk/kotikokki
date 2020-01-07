/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.service;

import java.util.List;
import kotikokki.domain.Yksikko;
import kotikokki.repository.YksikkoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pekka
 */
@Service
public class YksikkoService {
    @Autowired
    private YksikkoRepository yksRepo;
    
    public void tallenna(Yksikko yksikko){
        Yksikko y = yksRepo.findByNimi(yksikko.getNimi());
        if (y!=null){
            y.setNimi(yksikko.getNimi());
            y.setKerroin(yksikko.getKerroin());
            y.setPerusyksikko(yksikko.getPerusyksikko());
            y.setNimi_pitka(yksikko.getNimi_pitka());
            yksRepo.save(y);
        }
        else {
            yksRepo.save(yksikko);
        }
        
    }
    
    public List<Yksikko> findAll(){
        return yksRepo.findAll();
    }
    
    public void poista(Long id){
        yksRepo.deleteById(id);
    }
    
    public List<Yksikko> haeJarjestettyLista(){
        return yksRepo.findAllByOrderByNimiAsc();
    }
    
}
