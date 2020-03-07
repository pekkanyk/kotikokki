/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kotikokki.domain.RaakaAine;

/**
 *
 * @author Pekka
 */
public interface RaakaAineRepository extends JpaRepository<RaakaAine, Long>{
    RaakaAine findByNimi(String nimi);
    
}
