/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain;

import java.time.LocalDate;
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
    //@Column(columnDefinition = "integer default 0")
    //private int jsSaldo;
    //@Column(columnDefinition = "boolean default false")
    //private boolean isEol;

    
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
    
    
}
