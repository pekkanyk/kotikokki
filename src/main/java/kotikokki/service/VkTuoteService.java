/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.service;

import java.net.URL;
import java.util.Scanner;
import kotikokki.domain.vk.Tuotteet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kotikokki.domain.OutletHistoria;
import kotikokki.domain.OutletTuote;
import kotikokki.domain.vk.Product;
import kotikokki.domain.vk.Tuotteet;
import kotikokki.repository.OutletTuoteRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Pekka
 */
@Service
public class VkTuoteService {
    @Autowired
    private OutletTuoteRepository outletTuoteRepository;

    
    private String osoite ="https://web-api.service.verkkokauppa.com/search?context=customer_returns_page&pageNo=";
    
    private int haeSivumaara() throws MalformedURLException, IOException{
        String url = osoite +"0";
        String json = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
        Gson gson = new Gson();
        Tuotteet tuotteet = gson.fromJson(json, Tuotteet.class);
        return tuotteet.getNumPages();
    }
    
    private Tuotteet haeSivu(int num) throws IOException{
        String url = osoite +num;
        String json = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
        Gson gson = new Gson();
        Tuotteet tuotteet = gson.fromJson(json, Tuotteet.class);
        return tuotteet;
    }
    
    private List<OutletTuote>VkTuotteetToOutletTuote(Tuotteet tuotteet){
        List<OutletTuote> outletTuotteet = new ArrayList();
        List<Product> vkTuotteet = tuotteet.getProducts();
        for (Product product: vkTuotteet){
            OutletTuote outlet = new OutletTuote();
            outlet.setName(product.getCustomerReturnsInfo().getProduct_name());
            if (product.getPrice().getCurrent()!=0.0){
                outlet.setNorPrice(product.getPrice().getCurrent());
            }
            else outlet.setNorPrice(product.getPrice().getOriginal());
            
            outlet.setOutPrice(product.getCustomerReturnsInfo().getPrice_with_tax());
            outlet.setUpdated(true);
            outlet.setPriceUpdatedDate(LocalDate.now());
            outlet.setDumppituote(product.isIsFireSale());
            outlet.setPid(product.getCustomerReturnsInfo().getPid());
            outlet.setOutId(product.getCustomerReturnsInfo().getId());
            outlet.setPoistotuote(product.isActive());
            outlet.setCondition(product.getCustomerReturnsInfo().getCondition());
            Double alennusprosentti = 0.0;
            if (outlet.getNorPrice()!=0.0){
                alennusprosentti = 100.0*(1-(product.getCustomerReturnsInfo().getPrice_with_tax()/outlet.getNorPrice()));
                }
            outlet.setAlennus(alennusprosentti);
            outlet.setWarranty(product.getCustomerReturnsInfo().getWarranty());
            outletTuotteet.add(outlet);
            }
        return outletTuotteet;
    }
    
    private List<OutletTuote> haeKaikki() throws IOException{
        List<OutletTuote> kaikkiOutletit = new ArrayList();
        int page = this.haeSivumaara();
        for (int i=0;i<page;i++){
            for (OutletTuote outlet: this.VkTuotteetToOutletTuote(this.haeSivu(i))){
                kaikkiOutletit.add(outlet);
            }
        }
        return kaikkiOutletit;
    }
    
    @Transactional
    public void reloadDb() throws IOException{
        List<OutletTuote> kaikkiOutletit = this.haeKaikki();
        
        for (OutletTuote outlet: kaikkiOutletit){
            OutletTuote outletFromDb = outletTuoteRepository.findByOutId(outlet.getOutId());
            if (outletFromDb == null){
                outletFromDb = outlet;
            }
            else {
                if (outletFromDb.getOutPrice()!=outlet.getOutPrice()){
                    outletFromDb = outlet;
                }
                if (outletFromDb.getDeleted()!=null) outletFromDb.setDeleted(null);
                outlet.setPriceUpdatedDate(outletFromDb.getPriceUpdatedDate());
                outletFromDb = outlet;
            }
            
            outletTuoteRepository.save(outletFromDb);
         
        }
        
        for (OutletTuote ostettu:outletTuoteRepository.findByUpdated(false)){
         
            if (ostettu.getDeleted()==null) {
                ostettu.setDeleted(LocalDate.now());
            }
            
            outletTuoteRepository.save(ostettu);
        }
        
        for (OutletTuote out:outletTuoteRepository.findByDeletedNotNull()){
                out.setUpdated(false);
                outletTuoteRepository.save(out);
            }
        for (OutletTuote paivitetty:outletTuoteRepository.findByUpdated(true)){
         
            paivitetty.setUpdated(false);
            
            outletTuoteRepository.save(paivitetty);
        }
    }
    public List<OutletTuote> historiaLista(String sort){
        List<OutletHistoria> outletHistoria = new ArrayList();
        if (sort.equals("pid")) {
            return outletTuoteRepository.deletedListAllByPpid();
        }
        else if (sort.equals("date")){
            return outletTuoteRepository.deletedListAllByDelDate();
        }
        else {
            return outletTuoteRepository.deletedListAllByOutId();
        }
    }
    
    public long rivienLkm(String haku){
        if (haku.equals("active")) return outletTuoteRepository.countByDeletedIsNull();
        else return outletTuoteRepository.countByDeletedIsNotNull();
    }
    
    public List<OutletTuote> historiaHakuTarkkaPaiva(LocalDate date){
        return outletTuoteRepository.deletedListByDate(date);
    }
    
    public List<OutletTuote> historiahaku(String kohde, String haku){
        if (kohde.equals("date")) {
            LocalDate date = LocalDate.now().minusDays(Long.valueOf(haku));
            return outletTuoteRepository.deletedListByDate(date);
        }
        else if (kohde.equals("tuote")) return outletTuoteRepository.findByNameLikeIgnoreCaseAndDeletedIsNotNullOrderByNameAsc(haku);
        else return outletTuoteRepository.findByNameLikeIgnoreCaseAndDeletedIsNotNullOrderByNameAsc(haku);
    }
    
    public List<OutletTuote> listaaKaikkiAlleAlePaitsi(double ale, String haku){
        List<OutletTuote> outletit =outletTuoteRepository.findByNameNotLikeOrderByAlennusAsc(haku);
        return outletit;
    }
    
    public List<OutletTuote> listDbByOutId(String suunta){
        if (suunta.equals("asc")) return outletTuoteRepository.activeSearchAllSortOutIdDes();
        else return outletTuoteRepository.acticeSearchAllSortOutIdAsc();
    }
    
    public List<OutletTuote> alePros(Double arvo, String suunta){
        if (suunta.equals("asc")) {
            if (arvo==-999) return outletTuoteRepository.activeSearcAlennusAscNoApple(5, "Apple");
            else return outletTuoteRepository.activeSearcAlennusAsc(arvo);
        }
        else return outletTuoteRepository.activeSearcAlennusDes(arvo);
    }
    
    public List<OutletTuote> listByNimi(String haku){
        //return outletTuoteRepository.findByNameLikeIgnoreCaseOrderByNameAsc(haku);
        //return outletTuoteRepository.activeSearchName(haku);
        return outletTuoteRepository.findByNameLikeIgnoreCaseAndDeletedIsNullOrderByNameAsc(haku);
    }
    
    public List<OutletTuote> listByHinta(double hinta, String suunta){
        if (suunta.equals("des"))return outletTuoteRepository.activeSearchAllSortOutHintaDes(hinta);
        else return outletTuoteRepository.activeSearchAllSortOutHintaAsc(hinta);
    }
    
    public List<OutletTuote> listByPoisto(boolean poisto){
        return outletTuoteRepository.activeListAllVkActive(poisto);
    }
    public List<OutletTuote> listByDumppi(boolean dumppi){
        return outletTuoteRepository.activeListAllDumppi(dumppi);
    }
    public List<OutletTuote> listByMuuttunut(String pvSitten){
        LocalDate date = LocalDate.now().minusDays(Long.valueOf(pvSitten));
        return outletTuoteRepository.activeListAllSortChangeDateDesc(date);
    }
    /*
    public void delDb(){
        outletTuoteRepository.deleteAll();
    }
    */
}
