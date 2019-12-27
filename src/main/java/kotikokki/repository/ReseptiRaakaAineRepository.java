/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.repository;

import java.util.List;
import kotikokki.domain.Resepti;
import kotikokki.domain.ReseptiRaakaAine;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Pekka
 */
public interface ReseptiRaakaAineRepository extends JpaRepository<ReseptiRaakaAine, Long>{
    List<ReseptiRaakaAine> findByResepti(Resepti resepti);
    void deleteByResepti(Resepti resepti);
    
}
