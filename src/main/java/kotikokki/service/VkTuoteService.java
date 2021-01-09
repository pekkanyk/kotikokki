/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.service;

import java.net.URL;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import kotikokki.domain.OutletTuote;
import kotikokki.domain.PvmStatistiikka;
import kotikokki.domain.UpdateTime;
import kotikokki.domain.vk.Product;
import kotikokki.domain.vk.Tuotteet;
import kotikokki.repository.OutletTuoteRepository;
import kotikokki.repository.UpdateTimeRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;


/**
 *
 * @author Pekka
 */
@Service
public class VkTuoteService {
    @Autowired
    private OutletTuoteRepository outletTuoteRepository;
    @Autowired
    private UpdateTimeRepository updateTimeRepository;
    
    
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
        //Gson gson = new Gson();
        Gson gson = new GsonBuilder().serializeNulls().create();
        Tuotteet tuotteet = gson.fromJson(json, Tuotteet.class);
        return tuotteet;
    }
    
    private List<OutletTuote>VkTuotteetToOutletTuote(Tuotteet tuotteet){
        List<OutletTuote> outletTuotteet = new ArrayList();
        List<Product> vkTuotteet = tuotteet.getProducts();
        ZoneId zone = ZoneId.of("Europe/Helsinki");
        for (Product product: vkTuotteet){
            OutletTuote outlet = new OutletTuote();
            outlet.setName(product.getCustomerReturnsInfo().getProduct_name());
            if (product.getPrice().getCurrent()!=0.0){
                outlet.setNorPrice(product.getPrice().getCurrent());
            }
            else outlet.setNorPrice(product.getPrice().getOriginal());
            if (product.getPrice().getDiscount()!= null && product.getPrice().getDiscountAmount()>0) {
                outlet.setKampanja(true);
                outlet.setKamploppuu(product.getPrice().getDiscount().kampanjanLoppuPaiva());
                //outlet.setKamploppuu(LocalDate.now());
            }
            else outlet.setKampanja(false);
            outlet.setOutPrice(product.getCustomerReturnsInfo().getPrice_with_tax());
            outlet.setUpdated(true);
            outlet.setPriceUpdatedDate(LocalDate.now(zone));
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
            outlet.setFirstSeen(LocalDate.now(zone));
            if (product.getAvailability()!=null){
                outlet.setOnVarasto(product.getAvailability().isIsPurchasable());
                if (product.getAvailability().isHasStock()){
                    if (product.getAvailability().getStocks().getWeb()!=null)outlet.setVarastossa(product.getAvailability().getStocks().getWeb().getStock());
                    else outlet.setVarastossa(0);
                }
            }
            else outlet.setVarastossa(0);
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
                    outlet.setFirstSeen(outletFromDb.getFirstSeen());
                    outletFromDb = outlet;
                }
                if (outletFromDb.getDeleted()!=null) outletFromDb.setDeleted(null);
                outlet.setPriceUpdatedDate(outletFromDb.getPriceUpdatedDate());
                outlet.setFirstSeen(outletFromDb.getFirstSeen());
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
        this.listaHaettu();
    }
    
    public List<OutletTuote> historiaLista(String sort){
        //List<OutletHistoria> outletHistoria = new ArrayList();
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
    
    
    @Scheduled(fixedDelay = 3600000, initialDelay = 100000)
    public void automaattiReload() throws IOException {
	ZoneId zone = ZoneId.of("Europe/Helsinki");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime aamu = LocalTime.parse("05:30:00", dtf);
        LocalTime ilta = LocalTime.parse("23:59:00", dtf);
        if (LocalTime.now(zone).isAfter(aamu) && LocalTime.now(zone).isBefore(ilta)){
            this.reloadDb();
        }
        
    }
    
    public List<PvmStatistiikka> lisatytPvm(){
        List<PvmStatistiikka> paivamaarat = new ArrayList();
        for (LocalDate pvm:outletTuoteRepository.lisaysPaivamaarat()){
            if (outletTuoteRepository.lisaysPoistuneetPvm(pvm)==0) paivamaarat.add(new PvmStatistiikka(pvm,outletTuoteRepository.lisaysTuotemaaraPvm(pvm),this.kaikkiLisatytKeskimaarinAktiivisena(pvm),0.0,outletTuoteRepository.lisaysPoistuneetPvm(pvm)));
            else paivamaarat.add(new PvmStatistiikka(pvm,outletTuoteRepository.lisaysTuotemaaraPvm(pvm),this.kaikkiLisatytKeskimaarinAktiivisena(pvm),outletTuoteRepository.deletedLisaysPv(pvm),outletTuoteRepository.lisaysPoistuneetPvm(pvm)));
        }
        return paivamaarat;
    }
    
    public double lisatytKeskimaarinAktiivisena(){
        return outletTuoteRepository.deletedLisaysPvKaikkiKa();
    }
    
    public double kaikkiLisatytKeskimaarinAktiivisena(LocalDate pvm){
        double ka = 0.0;
        long paivia = 0;
        if (outletTuoteRepository.lisaysPoistuneetPvm(pvm)==0){
            paivia = outletTuoteRepository.activeLisaysPvKpl(pvm);
        }
        else {
            paivia =  outletTuoteRepository.deletedLisaysPvKpl(pvm) +outletTuoteRepository.activeLisaysPvKpl(pvm);
        }
        long tuotteita = outletTuoteRepository.lisaysTuotemaaraPvm(pvm);
        if (tuotteita!=0) ka = (1.0*paivia)/tuotteita;
                
        return ka;
    }
    
    
    public List<PvmStatistiikka> aktiivisetPvm(){
        List<PvmStatistiikka> paivamaarat = new ArrayList();
        double ale = 0.0;
        for (LocalDate pvm:outletTuoteRepository.activePaivamaarat()){
            ale = this.activeKaPvm(pvm);
            paivamaarat.add(new PvmStatistiikka(pvm,outletTuoteRepository.activeTuotemaaraPvm(pvm),ale,0.0));
        }
        return paivamaarat;
    }
    
    public List<PvmStatistiikka> deletedPvm(){
        List<PvmStatistiikka> paivamaarat = new ArrayList();
        double ale = 0.0;
        for (LocalDate pvm:outletTuoteRepository.deletedPaivamaarat()){
            ale = this.deletedKaPvm(pvm);
            paivamaarat.add(new PvmStatistiikka(pvm,outletTuoteRepository.deletedTuotemaaraPvm(pvm),ale,outletTuoteRepository.deletedMyyntiPv(pvm)));
        }
        return paivamaarat;
    }
    
    public long aktiivisetEriTuotteet(){
        return outletTuoteRepository.activeDistinctProducts();
    }
    
    public double activeKa(){
        double norPrice = outletTuoteRepository.activeSumNorPrice();
        if (norPrice == 0.0) norPrice = 0.001;
        return 100.0*(1-outletTuoteRepository.activeSumOutPrice()/norPrice);
    }
    
    public double deletedKa(){
        double norPrice = outletTuoteRepository.deletedSumNorPrice();
        if (norPrice == 0.0) norPrice = 0.001;
        return 100.0*(1-outletTuoteRepository.deletedSumOutPrice()/norPrice);
    }
    
    public double activeKaPvm(LocalDate date){
        double norPrice = outletTuoteRepository.activeSumNorPricePvm(date);
        if (norPrice == 0.0) norPrice = 0.001;
        return 100.0*(1-outletTuoteRepository.activeSumOutPricePvm(date)/norPrice);
    }
    
    public double deletedKaPvm(LocalDate date){
        double norPrice = outletTuoteRepository.deletedSumNorPricePvm(date);
        if (norPrice == 0.0) norPrice = 0.001;
        return 100.0*(1-outletTuoteRepository.deletedSumOutPricePvm(date)/norPrice);
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
    
    public List<OutletTuote> haePidMukaan(String haku, int pid){
        if (haku.equals("active")) return outletTuoteRepository.activeByPid(pid);
        else return outletTuoteRepository.deletedByPid(pid);
    }
    
    private void listaHaettu(){
        UpdateTime updated = new UpdateTime();
        ZoneId zone = ZoneId.of("Europe/Helsinki");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        updated.setHakuaika(LocalTime.now(zone).format(dtf).toString());
        int tid = 1000;
        updated.setTid(tid);
        updateTimeRepository.save(updated);
    }
    public String listaHaettuAika(){
        return updateTimeRepository.findByTid(1000).getHakuaika();
    }
    public List<OutletTuote> ennenPvYliHinnan(LocalDate date, double outPrice){
        return outletTuoteRepository.ennenYliHinnan(date, outPrice);
    }
    /*
    public void delDb(){
        outletTuoteRepository.deleteAll();
    }
    */
    public long daysBetween(LocalDate muutos, LocalDate poisto){
        long days = ChronoUnit.DAYS.between(muutos, poisto);
        return days;
    }
  
}
