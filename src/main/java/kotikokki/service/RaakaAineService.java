/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.service;

import kotikokki.domain.RaakaAine;
import kotikokki.repository.RaakaAineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pekka
 */
@Service
public class RaakaAineService {
    @Autowired
    private RaakaAineRepository raakaAineRepo;
    
    public RaakaAine lisaaJosEiOlemassa(String raakaAine){
        RaakaAine ra = new RaakaAine();
        
        if (raakaAineRepo.findByNimi(raakaAine)==null){
            ra.setNimi(raakaAine);
            return raakaAineRepo.save(ra);
        }
        return raakaAineRepo.findByNimi(raakaAine);
    }
}
