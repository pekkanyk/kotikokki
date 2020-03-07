/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode (callSuper = false)
public class RaakaAine extends AbstractPersistable<Long> {
    @Column(unique=true)
    private String nimi;
    @OneToMany(mappedBy="raakaAine")
    private List<ReseptiRaakaAine> reseptiRaakaAineet;
    private String kauppa_osasto;
}
