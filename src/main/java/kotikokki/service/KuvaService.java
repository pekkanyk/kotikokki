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
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Pekka
 */
@Service
public class KuvaService {
    @Autowired
    private KuvaRepository kuvaRepo;
    
    public Kuva getKuvaObject(Long id){
        return kuvaRepo.getOne(id);
    }
    
    public Kuva save(MultipartFile picture, Resepti resepti) throws IOException{
        Kuva k = new Kuva();
        byte[] bytes = IOUtils.toByteArray(picture.getInputStream());
        k.setKuva(bytes);
        k.setResepti(resepti);
        return kuvaRepo.save(k);
    }
    
    /*
    public ResponseEntity<byte[]> getPicture(Long id){
        Picture p = pictureRepository.getOne(id);
        
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(p.getContentType()));
        headers.setContentLength(p.getContentLength());
        
        return new ResponseEntity<>(p.getPicture(),headers,HttpStatus.CREATED);
    }
    
    public String save(MultipartFile picture, String text, Account account) throws IOException{
        Picture p = new Picture();
        p.setContentType(picture.getContentType());
        p.setText(text);
        p.setAccount(account);
        byte[] bytes = IOUtils.toByteArray(picture.getInputStream());
        p.setPicture(bytes);
        p.setContentLength(picture.getSize());
        pictureRepository.save(p);
        return "redirect:/gallery";
    }
    
    
    public List<Picture> findByAccount(Account account){
        return pictureRepository.findByAccount(account);
    }
    
    @Transactional
    public void deletePicture(Long id){
        Picture p = pictureRepository.getOne(id);
        comment_pictureRepository.deleteByPicture(p);
        pictureRepository.deleteById(id);
    }
    */
    
    
}
