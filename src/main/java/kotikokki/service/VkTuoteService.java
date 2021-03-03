/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.service;

import java.net.URL;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
//import org.springframework.data.domain.Example;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import kotikokki.domain.Koot;
import kotikokki.domain.OutletTuote;
import kotikokki.domain.PidPackage;
import kotikokki.domain.PvmStatistiikka;
import kotikokki.domain.UpdateTime;
import kotikokki.domain.vk.Product;
import kotikokki.domain.vk.Tuotteet;
import kotikokki.repository.OutletTuoteRepository;
import kotikokki.repository.PidPackageRepository;
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
    @Autowired
    private PidPackageRepository pidPackageRepository;
    
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
            PidPackage pidPackage = new PidPackage();
            if (pidPackageRepository.findByPid(product.getPid())!=null){
                pidPackage = pidPackageRepository.findByPid(product.getPid());
            }
            else {
                pidPackage.setPid(product.getPid());
                pidPackage.setWidth(product.getPackageName().getWidth());
                pidPackage.setHeight(product.getPackageName().getHeight());
                pidPackage.setDepth(product.getPackageName().getDepth());
                pidPackage.setVolume(product.getPackageName().getVolume());
                pidPackage.setWeight(product.getPackageName().getWeight());
                
            }
            pidPackageRepository.save(pidPackage);
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
            outlet.setPidLuotu(LocalDate.parse(product.getCreatedAt().substring(0, 10)));
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
            outletFromDb.setKoko(this.paketinKoko(pidPackageRepository.findByPid(outletFromDb.getPid())));
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
    
    public List<OutletTuote> haeNimellaAlkaenAsti(String haku, String activity, String sortStr, String alkaen, String asti){
        List<OutletTuote> outletTuotteet = new ArrayList();
        Sort sort = Sort.by("pid").and(Sort.by("deleted").descending()).and(Sort.by("priceUpdatedDate").descending());
        if (sortStr.equals("price")){
            sort = Sort.by("outPrice").and(sort);    
        }
        else if (sortStr.equals("priceD")){
            sort = Sort.by("outPrice").descending().and(sort);    
        }
        else if(sortStr.equals("ale")){
            sort = Sort.by("alennus").and(sort);
        }
        else if(sortStr.equals("aleD")){
            sort = Sort.by("alennus").descending().and(sort);
        }
        else if(sortStr.equals("date")){
            sort = Sort.by("deleted").descending().and(Sort.by("priceUpdatedDate").descending()); 
        }
        else if(sortStr.equals("dateD")){
            sort = Sort.by("deleted").and(Sort.by("priceUpdatedDate")); 
        }
        else if(sortStr.equals("outId")){
            sort = Sort.by("outId"); 
        }
        else if(sortStr.equals("outIdD")){
            sort = Sort.by("outId").descending(); 
        }
        else if (sortStr.equals("pid") && !activity.equals("both")){
            sort = Sort.by("pid");
        }
        else if (sortStr.equals("pidD") && !activity.equals("both")){
            sort = Sort.by("pid").descending();
        }
        LocalDate from = LocalDate.parse("2020-01-01");
        LocalDate to = LocalDate.now();
        if (!alkaen.equals("")) from = LocalDate.parse(alkaen);
        if (!asti.equals("")) to = LocalDate.parse(asti);
        
        haku = "%"+haku+"%";
        if (activity.equals("active")){
            outletTuotteet = outletTuoteRepository.haeNimellaAktiivisistaAlkaenAsti(haku, from, to, sort);
        }
        else if (activity.equals("deleted")){
            outletTuotteet = outletTuoteRepository.haeNimellaPoistuneistaAlkaenAsti(haku, from, to, sort);
        }
        else {
            outletTuotteet = outletTuoteRepository.haeNimellaKaikistaAlkaenAsti(haku, from, to, sort);
        }
        
        return outletTuotteet;
    }
    
    public double keskiArvoAlennusAlkaenAsti(String haku, String activity, String alkaen, String asti){
        LocalDate from = LocalDate.parse("2020-01-01");
        LocalDate to = LocalDate.now();
        if (!alkaen.equals("")) from = LocalDate.parse(alkaen);
        if (!asti.equals("")) to = LocalDate.parse(asti);
        double alePros = 0.0;
        Double outHinnat =0.0;
        Double norHinnat =0.0;
        haku = "%"+haku+"%";
        if (activity.equals("deleted")){
            outHinnat = outletTuoteRepository.haeNimellaSummaaOutPriceDeleted(haku, from, to);
            norHinnat = outletTuoteRepository.haeNimellaSummaaNorPriceDeleted(haku, from, to);
        }
        else if (activity.equals("active")){
            outHinnat = outletTuoteRepository.haeNimellaSummaaOutPriceActive(haku, from, to);
            norHinnat = outletTuoteRepository.haeNimellaSummaaNorPriceActive(haku, from, to);
        }
        else {
            outHinnat = outletTuoteRepository.haeNimellaSummaaOutPriceBoth(haku, from, to);
            norHinnat = outletTuoteRepository.haeNimellaSummaaNorPriceBoth(haku, from, to);
        }
        
        if (norHinnat == null ) norHinnat = 0.0;
        if (outHinnat == null ) outHinnat = 0.0;
        if (norHinnat>0.0){
            alePros = 100*(1-(outHinnat)/norHinnat);
        }
        
        return alePros;
    }
    public double keskiArvoAlennusAlkaenAstiKl(String haku, String activity, String alkaen, String asti,String condition){
        LocalDate from = LocalDate.parse("2020-01-01");
        LocalDate to = LocalDate.now();
        if (!alkaen.equals("")) from = LocalDate.parse(alkaen);
        if (!asti.equals("")) to = LocalDate.parse(asti);
        double alePros = 0.0;
        Double outHinnat =0.0;
        Double norHinnat =0.0;
        haku = "%"+haku+"%";
        if (activity.equals("deleted")){
            outHinnat = outletTuoteRepository.haeNimellaSummaaOutPriceDeletedKl(haku, from, to, condition);
            norHinnat = outletTuoteRepository.haeNimellaSummaaNorPriceDeletedKl(haku, from, to, condition);
        }
        else if (activity.equals("active")){
            outHinnat = outletTuoteRepository.haeNimellaSummaaOutPriceActiveKl(haku, from, to, condition);
            norHinnat = outletTuoteRepository.haeNimellaSummaaNorPriceActiveKl(haku, from, to, condition);
        }
        else {
            outHinnat = outletTuoteRepository.haeNimellaSummaaOutPriceBothKl(haku, from, to, condition);
            norHinnat = outletTuoteRepository.haeNimellaSummaaNorPriceBothKl(haku, from, to, condition);
        }
        
        if (norHinnat == null ) norHinnat = 0.0;
        if (outHinnat == null ) outHinnat = 0.0;
        if (norHinnat>0.0){
            alePros = 100*(1-(outHinnat)/norHinnat);
        }
        
        return alePros;
    }
    
    public String aletkuntoluokittain(String haku, String activity, String alkaen, String asti){
        String kuntoluokat[] = {"A","B","C","D"};
        String uloste = "";
        for (String kl:kuntoluokat){
            Double kaAle = this.keskiArvoAlennusAlkaenAstiKl(haku, activity, alkaen, asti, kl);
            if (kaAle!=0){
                uloste = uloste+kl+": "+String.format("%.2f", kaAle)+"% ";
            }
            
        }
        return uloste;
    }
    
    public Double outPriceSumma(String haku, String activity, String alkaen, String asti){
        LocalDate from = LocalDate.parse("2020-01-01");
        LocalDate to = LocalDate.now();
        if (!alkaen.equals("")) from = LocalDate.parse(alkaen);
        if (!asti.equals("")) to = LocalDate.parse(asti);
        Double summa = 0.0;
        haku = "%"+haku+"%";
        if (activity.equals("deleted")){
            summa=outletTuoteRepository.haeNimellaSummaaOutPriceDeleted(haku, from, to);
        }
        else if (activity.equals("active")){
            summa=outletTuoteRepository.haeNimellaSummaaOutPriceActive(haku, from, to);
        }
        else {
            summa=outletTuoteRepository.haeNimellaSummaaOutPriceBoth(haku, from, to);
        }
        return summa;
    }
    
    public List<OutletTuote> haePidMukaan(int pid, String activity){
        Sort sort = Sort.by(Sort.Direction.ASC,"pid");
        
        if (activity.equals("active")) {
            return outletTuoteRepository.activeByPidTEST(pid,sort);
        }
        else if (activity.equals("deleted")) return outletTuoteRepository.deletedByPid(pid);
        else return outletTuoteRepository.allbyPid(pid);
    }
    
    
    public List<OutletTuote> yliPaivaaSittenMyyty(String days){
        List<OutletTuote> tuotteet = new ArrayList();
        long paivia = Long.parseLong(days);
        boolean listattava = false;
        for (int outId:outletTuoteRepository.activeDistinctProductPids()){
            listattava = false;
            if ((outletTuoteRepository.countPidAll(outId)>1 && outletTuoteRepository.countPidDeleted(outId)>0)||(outletTuoteRepository.countPid(outId)>=1)){
                listattava = false;
                for (OutletTuote outTuote:outletTuoteRepository.activeByPid(outId)){
                    if (outTuote.getPriceUpdatedDate().isBefore(LocalDate.now().minusDays(paivia+1))){
                        listattava = true;
                        break;
                    }
                    
                }
                if (listattava){
                    for (OutletTuote outTuote:outletTuoteRepository.deletedByPid(outId)){
                        if (outTuote.getDeleted().isAfter(LocalDate.now().minusDays(paivia))){
                        listattava = false;
                        }
                    }
                }
                
                if (listattava) tuotteet.addAll(outletTuoteRepository.allbyPid(outId));
            }
        }
        return tuotteet;
    }
    
    private String paketinKoko(PidPackage pp){
            String koko = "";
            if (pp.getVolume()<=1200000) koko = "P";
            else if (pp.getVolume()<=10000000) koko = "K";
            else koko = "I";
            return koko;
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
    
    
    //@Scheduled(fixedDelay = 3600000, initialDelay = 100000)
    //@Scheduled(cron = "0 58 5-17,23 * * ?", zone = "Europe/Helsinki")
    @Scheduled(cron = "0 8/10 5-23 * * ?", zone = "Europe/Helsinki")
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
    
    public double keskimaarinAktiivisena(){
        long paivatYhteensa = outletTuoteRepository.activeDays()+outletTuoteRepository.deletedDays();
        double ka = 0.0;
        long riveja = outletTuoteRepository.countAll();
        if (riveja>0) ka = (1.0*paivatYhteensa)/riveja;
        return ka;
    }
    
    public List<OutletTuote> aktiivisiaPvm(LocalDate pvm){
        return outletTuoteRepository.activeForDate(pvm);
    }
    public List<OutletTuote> firstSeenPvm(LocalDate pvm){
        return outletTuoteRepository.firstSeenForDate(pvm);
    }
        
    public double kaikkiLisatytKeskimaarinAktiivisena(LocalDate pvm){
        double ka = 0.0;
        long paivia = 0;
        long poistuneita = outletTuoteRepository.lisaysPoistuneetPvm(pvm);
        long aktiivisia = outletTuoteRepository.lisaysAktiivisetPvm(pvm);
        if (poistuneita ==0){
            paivia = outletTuoteRepository.activeLisaysPvKpl(pvm);
        }
        else if (aktiivisia==0){
            paivia = outletTuoteRepository.deletedLisaysPvKpl(pvm);
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
    
    public List<OutletTuote> historiaAlkaenLoppuu(String haku, String asti, String alkaen){
        LocalDate from = LocalDate.parse(alkaen);
        LocalDate to = LocalDate.parse(asti);
        return outletTuoteRepository.deletedSearchNameWithDate(haku, to, from);
    }
    public double keskiArvoAlennus(String haku, String asti, String alkaen){
        LocalDate from = LocalDate.parse(alkaen);
        LocalDate to = LocalDate.parse(asti);
        double alePros = 0.0;
        Double outHinnat = outletTuoteRepository.summaaOutPriceDeleted(haku,to,from);
        Double norHinnat = outletTuoteRepository.summaaNorPriceDeleted(haku, to, from);
        if (norHinnat == null ) norHinnat = 0.0;
        if (outHinnat == null ) outHinnat = 0.0;
        //int riveja = outletTuoteRepository.deletedSearchNameWithDate(haku, to, from).size();
        if (norHinnat>0.0){
            alePros = 100*(1-(outHinnat)/norHinnat);
        }
        
        return alePros;
    }
    
    public String deletedProssatKuntoluokilla(){
        String alet = "";
        Double alePros = 0.0;
        for (String kl:outletTuoteRepository.kuntoluokat()){
            alePros = 100*(1-(outletTuoteRepository.summaaPoistuneetOutPriceKuntoluokalla(kl)/outletTuoteRepository.summaaPoistuneetNorPriceKuntoluokalla(kl)));
            alet = alet+" "+kl+": "+String.format("%.2f", alePros)+"%";
        }
        return alet;
    }
    
    public List<OutletTuote> historiahaku(String kohde, String haku){
        if (kohde.equals("date")) {
            LocalDate date = LocalDate.now().minusDays(Long.valueOf(haku));
            return outletTuoteRepository.deletedListByDate(date);
        }
        else if (kohde.equals("tuote")) {
            return outletTuoteRepository.findByNameLikeIgnoreCaseAndDeletedIsNotNullOrderByNameAsc(haku);
        }
        
        else return outletTuoteRepository.findByNameLikeIgnoreCaseAndDeletedIsNotNullOrderByNameAsc(haku);
    }
    
    public List<OutletTuote> yliXaktiivista(int lkm,String date){
        List<OutletTuote> tuotteet = new ArrayList();
        boolean oldEnough = false;
        for (int pid:outletTuoteRepository.activeDistinctProductPids()){
            oldEnough=false;
            if (outletTuoteRepository.countPid(pid)>=lkm){
                for (OutletTuote out:outletTuoteRepository.activeByPid(pid)){
                    if (out.getPriceUpdatedDate().minusDays(1).isBefore(LocalDate.parse(date))){
                        oldEnough = true;
                    }
                }
                if (oldEnough) tuotteet.addAll(outletTuoteRepository.activeByPid(pid));
            }
        }
        return tuotteet;
    }
    
    public List<OutletTuote> dumppiVarastolla(){
        return outletTuoteRepository.activeDumppiVarastolla();
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
            if (arvo==-999) return outletTuoteRepository.activeSearcAlennusAscNoApple(10, "Apple");
            else return outletTuoteRepository.activeSearcAlennusAsc(arvo);
        }
        else return outletTuoteRepository.activeSearcAlennusDes(arvo);
    }
    
    public List<OutletTuote> listByNimi(String haku){
        //return outletTuoteRepository.findByNameLikeIgnoreCaseOrderByNameAsc(haku);
        //return outletTuoteRepository.activeSearchName(haku);
        //return outletTuoteRepository.findByNameLikeIgnoreCaseAndDeletedIsNullOrderByNameAsc(haku);
        //return outletTuoteRepository.findByNameLikeIgnoreCaseOrderByNameAsc(haku);
        return outletTuoteRepository.searchByName(haku);
    }
    
    public List<Koot> countLastNbrs(){
        List<Koot> numerot = new ArrayList();
        
        for (int i=0;i<=9;i++){
            Koot kokostats = new Koot();
            kokostats.setHylly(i);
            kokostats.setPieni(outletTuoteRepository.countLastNumberOfOutIdIsSize(String.valueOf(i),"P"));
            kokostats.setKeski(outletTuoteRepository.countLastNumberOfOutIdIsSize(String.valueOf(i),"K"));
            kokostats.setIso(outletTuoteRepository.countLastNumberOfOutIdIsSize(String.valueOf(i),"I"));
            numerot.add(kokostats);
        }
        return numerot;
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
    
  
}
