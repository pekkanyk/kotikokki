/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import kotikokki.domain.OutletTuote;

/**
 *
 * @author Pekka
 */
public interface OutletTuoteRepository extends JpaRepository<OutletTuote, Long>{
    OutletTuote findByOutId(int id);
    public List<OutletTuote> findAllByOrderByAlennusAsc();
    public List<OutletTuote> findByAlennusLessThanOrderByAlennusAsc(double limit);
    public List<OutletTuote> findByNameNotLikeOrderByAlennusAsc(String haku);
    public List<OutletTuote> findByOutPriceLessThanOrderByOutPriceDesc(double hinta);
    public List<OutletTuote> findAllByOrderByOutIdAsc();
    public List<OutletTuote> findByPoistotuoteOrderByName(boolean poisto);
    public List<OutletTuote> findByDumppituoteOrderByName(boolean dumppi);
    public List<OutletTuote> findByPriceUpdatedDateBeforeOrderByNameAsc(LocalDate pvSitten);
    public List<OutletTuote> findByNameLikeIgnoreCaseOrderByNameAsc(String haku);
    public void deleteByUpdated(boolean updated);
    public List<OutletTuote> findByUpdated(boolean updated);
    //public List<OutletTuote> findAllByOrderByName();
}
