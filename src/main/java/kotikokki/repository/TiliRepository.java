/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.repository;

import kotikokki.domain.Tili;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Pekka
 */
public interface TiliRepository extends JpaRepository<Tili, Long>{
    Tili findByUsername(String username);
    Long countByUsername();
}
