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
import kotikokki.repository.OutletHistoriaRepository;
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
    @Autowired
    private OutletHistoriaRepository outletHistoriaRepository;
    
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
            //outlet.setIsEol(false);
            //if(product.getAvailability().isIsEOL()) outlet.setIsEol(true);
            //outlet.setJsSaldo(product.getAvailability().getStocks().getWeb().getWarehouses().getJs().getStock());
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
        //outletTuoteRepository.deleteAll();
        
        
        for (OutletTuote outlet: kaikkiOutletit){
            OutletTuote outletFromDb = outletTuoteRepository.findByOutId(outlet.getOutId());
            if (outletFromDb == null){
                outletFromDb = outlet;
            }
            else {
                if (outletFromDb.getOutPrice()!=outlet.getOutPrice()){
                    outletFromDb = outlet;
                }
                outlet.setPriceUpdatedDate(outletFromDb.getPriceUpdatedDate());
                outletFromDb = outlet;
            }
            
            outletTuoteRepository.save(outletFromDb);
            if (outletHistoriaRepository.findByOutId(outletFromDb.getOutId())!= null){
                outletHistoriaRepository.deleteByOutId(outletFromDb.getOutId());
            }
        }
        for (OutletTuote ostettu:outletTuoteRepository.findByUpdated(false)){
            OutletHistoria historia = new OutletHistoria();
            historia.setName(ostettu.getName());
            historia.setOutId(ostettu.getOutId());
            historia.setPid(ostettu.getPid());
            historia.setOutPrice(ostettu.getOutPrice());
            historia.setDeleteDay(LocalDate.now());
            outletHistoriaRepository.save(historia);
        }
        outletTuoteRepository.deleteByUpdated(false);
        for (OutletTuote out:outletTuoteRepository.findAll()){
                out.setUpdated(false);
                outletTuoteRepository.save(out);
            }

    }
    public List<OutletHistoria> historiaLista(String sort){
        List<OutletHistoria> outletHistoria = new ArrayList();
        if (sort.equals("pid")) {
            outletHistoria = outletHistoriaRepository.findAllByOrderByPid();
        }
        else if (sort.equals("date")){
            outletHistoria = outletHistoriaRepository.findAllByOrderByDeleteDay();
        }
        else {
            outletHistoria = outletHistoriaRepository.findAllByOrderByName();
        }
        return outletHistoria;
    }
    
    public List<OutletHistoria> historiahaku(String kohde, String haku){
        List<OutletHistoria> outletHistoria = new ArrayList();
        if (kohde.equals("date")) {
            outletHistoria = outletHistoriaRepository.findByDeleteDayOrderByPid(LocalDate.parse(haku));
        }
        else if (kohde.equals("tuote")){
            outletHistoria = outletHistoriaRepository.findByNameLikeIgnoreCaseOrderByNameAsc(haku);
        }
        else {
            outletHistoria = outletHistoriaRepository.findAllByOrderByName();
        }
        return outletHistoria;
    }
    
    public List<OutletTuote> listaaKaikkiAlleAlePaitsi(double ale, String haku){
        List<OutletTuote> outletit =outletTuoteRepository.findByNameNotLikeOrderByAlennusAsc(haku);
        return outletit;
    }
    
    public List<OutletTuote> listDbByOutId(String suunta){
        if (suunta.equals("des")) {
            List<OutletTuote> outletit = outletTuoteRepository.findAllByOrderByOutIdAsc();
            Collections.reverse(outletit);
            return outletit;
        }
        else return outletTuoteRepository.findAllByOrderByOutIdAsc();
    }
    public List<OutletTuote> alePros(Double arvo, String suunta){
        if (suunta.equals("des")){
            List<OutletTuote> outletit = outletTuoteRepository.findByAlennusLessThanOrderByAlennusAsc(arvo);
            Collections.reverse(outletit);
            return outletit;
        }
        else return outletTuoteRepository.findByAlennusLessThanOrderByAlennusAsc(arvo);
    }
    public List<OutletTuote> listByNimi(String haku){
        return outletTuoteRepository.findByNameLikeIgnoreCaseOrderByNameAsc(haku);
    }
    
    public List<OutletTuote> listByHinta(double hinta, String suunta){
        if (suunta.equals("asc")){
            List<OutletTuote> outletit = outletTuoteRepository.findByOutPriceLessThanOrderByOutPriceDesc(hinta);
            Collections.reverse(outletit);
            return outletit;
        }
        else return outletTuoteRepository.findByOutPriceLessThanOrderByOutPriceDesc(hinta);
    }
    public List<OutletTuote> listByPoisto(boolean poisto){
        return outletTuoteRepository.findByPoistotuoteOrderByName(poisto);
    }
    public List<OutletTuote> listByDumppi(boolean dumppi){
        return outletTuoteRepository.findByDumppituoteOrderByName(dumppi);
    }
    public List<OutletTuote> listByMuuttunut(LocalDate pvSitten){
        return outletTuoteRepository.findByPriceUpdatedDateBeforeOrderByNameAsc(pvSitten);
    }
    
    public void delDb(){
        outletTuoteRepository.deleteAll();
    }
}
