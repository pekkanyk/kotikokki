/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author Pekka
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Kuva extends AbstractPersistable<Long> {
    //@Type(type = "org.hibernate.type.BinaryType")
    @Lob
    private byte[] kuva;
    private String contentType;
    private Long contentLength;
    @OneToOne(mappedBy="kuva")
    private Resepti resepti;
}
