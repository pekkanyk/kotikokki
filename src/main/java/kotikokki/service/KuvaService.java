/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.service;

import java.io.IOException;
import kotikokki.domain.Kuva;
import kotikokki.domain.Resepti;
import kotikokki.repository.KuvaRepository;
import kotikokki.repository.ReseptiRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Pekka
 */
@Service
public class KuvaService {
    @Autowired
    private KuvaRepository kuvaRepo;
    @Autowired
    private ReseptiRepository reseptiRepo;
    
    public Kuva getKuvaObject(Long id){
        return kuvaRepo.getOne(id);
    }
    
    public void save(MultipartFile picture, Resepti resepti) throws IOException{
        Kuva k = new Kuva();
        Resepti r = resepti;
        k.setContentType(picture.getContentType());
        byte[] bytes = IOUtils.toByteArray(picture.getInputStream());
        k.setKuva(bytes);
        k.setContentLength(picture.getSize());
        r.setKuva(kuvaRepo.save(k));
        reseptiRepo.save(r);
    }
    
    
    public ResponseEntity<byte[]> haeKuva(Long id){
        Kuva k = kuvaRepo.getOne(id);
        
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(k.getContentType()));
        headers.setContentLength(k.getContentLength());
        return new ResponseEntity<>(k.getKuva(),headers,HttpStatus.CREATED);
    }
    
    @Transactional
    public void poistaKuvaReseptilta(Long id){
        Resepti r = reseptiRepo.getOne(id);
        kuvaRepo.deleteById(r.getKuva().getId());
        r.setKuva(null);
        reseptiRepo.save(r);
    }

}
