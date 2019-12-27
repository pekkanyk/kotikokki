/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.repository;

import java.util.List;
import kotikokki.domain.Resepti;
import kotikokki.domain.Tili;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Pekka
 */
public interface ReseptiRepository extends JpaRepository<Resepti, Long>{
    List<Resepti> findByTili(Tili tili);
    List<Resepti> findByJulkinen(boolean julkinen);
    List<Resepti> findByTiliOrJulkinen(Tili tili, boolean julkinen);
    
}
