/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.repository;

import kotikokki.domain.UpdateTime;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Pekka
 */
public interface UpdateTimeRepository extends JpaRepository<UpdateTime, Long>{
    UpdateTime findByTid(int tid);
    
}
