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
public class OutletHistoria{
    
    @Id private int outId;
    private int pid;
    //private int outId;
    private String name;
    private double outPrice;
    private LocalDate deleteDay;

    public int getOutId() {
        return outId;
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

    public LocalDate getDeleteDay() {
        return deleteDay;
    }

    public void setDeleteDay(LocalDate deleteDay) {
        this.deleteDay = deleteDay;
    }
    
}
