/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import kotikokki.domain.OutletHistoria;

/**
 *
 * @author Pekka
 */
public interface OutletHistoriaRepository extends JpaRepository<OutletHistoria, Long>{
    OutletHistoria findByOutId(int id);
    public List<OutletHistoria> findByNameLikeIgnoreCaseOrderByNameAsc(String haku);
    public List<OutletHistoria> findAllByOrderByName();
    public List<OutletHistoria> findAllByOrderByPid();
    public List<OutletHistoria> findAllByOrderByDeleteDay();
    public List<OutletHistoria> findByDeleteDayOrderByPid(LocalDate date);
    public void deleteByOutId(int id);
}
