/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author Pekka
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

//public class OutletTuote extends AbstractPersistable<Long>{
public class OutletTuote{
    
    @Id private int outId;
    //private int outId;
    private int pid;
    private String name;
    private double outPrice;
    private double norPrice;
    private LocalDate priceUpdatedDate;
    private boolean updated;
    private boolean poistotuote; // oikeasti isActive
    private boolean dumppituote;
    private double alennus;
    private int warranty;
    private String condition;
    private LocalDate deleted;
    private boolean kampanja;
    private LocalDate kamploppuu;
    private LocalDate firstSeen;
    private int varastossa;
    private boolean onVarasto;
    private String koko;
    private LocalDate pidLuotu;

    public LocalDate getKamploppuu() {
        return kamploppuu;
    }

    public void setKamploppuu(LocalDate kamploppuu) {
        this.kamploppuu = kamploppuu;
    }

    public LocalDate getPidLuotu() {
        return pidLuotu;
    }

    public void setPidLuotu(LocalDate pidLuotu) {
        this.pidLuotu = pidLuotu;
    }
    
    
    public int getVarastossa() {
        return varastossa;
    }

    public boolean isOnVarasto() {
        return onVarasto;
    }

    public void setOnVarasto(boolean onVarasto) {
        this.onVarasto = onVarasto;
    }
    
    
    public void setVarastossa(int varastossa) {
        this.varastossa = varastossa;
    }

    public String getKoko() {
        return koko;
    }

    public void setKoko(String koko) {
        this.koko = koko;
    }

    
    
    public LocalDate getFirstSeen() {
        return firstSeen;
    }

    public void setFirstSeen(LocalDate firstSeen) {
        this.firstSeen = firstSeen;
    }
    
    

    public boolean isKampanja() {
        return kampanja;
    }

    public void setKampanja(boolean kampanja) {
        this.kampanja = kampanja;
    }

    public LocalDate getDeleted() {
        return deleted;
    }

    public void setDeleted(LocalDate deleted) {
        this.deleted = deleted;
    }

    
    public int getWarranty() {
        return warranty;
    }

    public void setWarranty(int warranty) {
        this.warranty = warranty;
    }
    
    
    public int getOutId() {
        return outId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    public boolean getDumppituote(){
        return this.dumppituote;
    }
    public boolean getPoistotuote(){
        return this.poistotuote;
    }
    
    public void setOutId(int outId) {
        this.outId = outId;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOutPrice() {
        return outPrice;
    }

    public void setOutPrice(double outPrice) {
        this.outPrice = outPrice;
    }

    public double getNorPrice() {
        return norPrice;
    }

    public void setNorPrice(double norPrice) {
        this.norPrice = norPrice;
    }

    public LocalDate getPriceUpdatedDate() {
        return priceUpdatedDate;
    }

    public void setPriceUpdatedDate(LocalDate priceUpdatedDate) {
        this.priceUpdatedDate = priceUpdatedDate;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public boolean isPoistotuote() {
        return poistotuote;
    }

    public void setPoistotuote(boolean poistotuote) {
        this.poistotuote = poistotuote;
    }

    public boolean isDumppituote() {
        return dumppituote;
    }

    public void setDumppituote(boolean dumppituote) {
        this.dumppituote = dumppituote;
    }

    public double getAlennus() {
        return alennus;
    }

    public void setAlennus(double alennus) {
        this.alennus = alennus;
    }
    
    public String daysActive(){
        String days = "-";
        if (firstSeen.isAfter(LocalDate.parse("2020-12-22"))){
            if (this.deleted==null){
                days = String.valueOf(ChronoUnit.DAYS.between(firstSeen, LocalDate.now(ZoneId.of("Europe/Helsinki"))))+"/"+
                        String.valueOf(ChronoUnit.DAYS.between(priceUpdatedDate, LocalDate.now(ZoneId.of("Europe/Helsinki"))));
            }
            else {
                days = String.valueOf(ChronoUnit.DAYS.between(firstSeen, deleted))+"/"+String.valueOf(ChronoUnit.DAYS.between(priceUpdatedDate, deleted));
            }
        }
        return days;
    }
    
    public String daysActiveDeleted(){
        String days="-";
        if (firstSeen.isAfter(LocalDate.parse("2020-12-22"))){
            days = String.valueOf(ChronoUnit.DAYS.between(firstSeen, deleted));
        }
        return days;
    }
    
    public String isDeleted(){
        String del = "active";
        if (this.deleted!=null) del = "deleted";
        return del;
    }
    
    public String changedOrDeleted(){
        String date = priceUpdatedDate.toString();
        if (deleted!=null) date = deleted.toString();
        return date;
    }
    
    public String similarName(){
        String[] osat = name.split(",");
        String hakunimi = osat[0].replaceAll("[0-9]", "%25");
        hakunimi = hakunimi.replaceAll("\\+", "%2B"); //%2B = + merkki haettaessa
        hakunimi = hakunimi.replaceAll(" ", "+");
        
        return hakunimi;
    }
    
}
