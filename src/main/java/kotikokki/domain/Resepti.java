/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
public class Resepti extends AbstractPersistable<Long> {
    private String nimi;
    private String ohje;
    private LocalDateTime lisatty;
    @OneToOne
    private Kuva kuva;
    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private boolean julkinen;
    @ElementCollection
    private List<String> kategoria;
    
    @OneToMany(mappedBy="resepti")
    private List<ReseptiRaakaAine> reseptiRaakaAineet;
    @ManyToOne
    private Tili tili;

}
