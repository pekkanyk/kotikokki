/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.repository;

import java.util.List;
import kotikokki.domain.Yksikko;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Pekka
 */
public interface YksikkoRepository extends JpaRepository<Yksikko, Long>{
    public List<Yksikko> findAllByOrderByNimiAsc();
    public Yksikko findByNimi(String nimi);
}
