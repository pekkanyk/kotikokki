/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
public class ReseptiRaakaAine extends AbstractPersistable<Long> {
    @ManyToOne
    private RaakaAine raakaAine;
    @ManyToOne
    private Resepti resepti;
    private Double maara;
    private String yksikko;
    
    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
            
        final ReseptiRaakaAine other = (ReseptiRaakaAine) obj;
        return ((this.resepti.equals(other.resepti) && this.maara.equals(other.maara)) && 
                    (this.raakaAine.equals(other.raakaAine) && this.yksikko.equals(other.yksikko)));
    }
    @Override
    public int hashCode() {
        return raakaAine.hashCode()+resepti.hashCode()+maara.hashCode()+yksikko.hashCode();
    }
}
