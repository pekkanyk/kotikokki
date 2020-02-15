/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import kotikokki.domain.Tili;
import kotikokki.repository.TiliRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pekka
 */
@Service
public class TiliService {
    @Autowired
    private TiliRepository tiliRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public String luoTili(String username, String password, String password2, String kokoNimi){
        String error = "";
        if (!password.equals(password2)){
            error = "salasanat eivät olleet samat. Ytitä uudelleen.";
            return error;
        }
        if (this.tiliRepo.findByUsername(username)==null){
            
            Tili t = new Tili();
            t.setUsername(username);
            t.setPassword(passwordEncoder.encode(password));
            t.setKokoNimi(kokoNimi);
            tiliRepo.save(t);
            error = "Tili luotu. Kirjaudu sisään jatkaaksesi.";
            return error;
        }
        else {
            error = "Käyttäjätunnus on jo olemassa. Valitse toinen käyttäjätunnus.";
            return error;
        }
    }
    
    public Tili haeTiliIdlla(Long id){
        return tiliRepo.getOne(id);
    }
    
    public Tili haeTiliKayttajanimella(String username){
        return tiliRepo.findByUsername(username);
    }
    
    public Long laskeKayttajat(){
        return tiliRepo.count();
    }
    
}
