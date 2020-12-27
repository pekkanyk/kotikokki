/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import kotikokki.domain.OutletTuote;

/**
 *
 * @author Pekka
 */
public interface OutletTuoteRepository extends JpaRepository<OutletTuote, Long>{
    //haku outId mukaan
    OutletTuote findByOutId(int id);
    OutletTuote findByOutIdAndDeletedIsNull(int id);
    
    //aktiivisten lkm
    long countByDeletedIsNull();
    
    //poistettujen lkm
    long countByDeletedIsNotNull();
    
    //aktiivisten haku nimen mukaan, sorttaus nimen mukaan
    @Query ("SELECT o FROM OutletTuote o WHERE name LIKE LOWER(?1) ORDER BY o.name")  // ei toimi
    List<OutletTuote> activeSearchName(String haku);
    List<OutletTuote> findByNameLikeIgnoreCaseAndDeletedIsNullOrderByNameAsc(String haku);
    
    //hidden feat nro2. (ale -999) <x% outID:n mukaan, ei Applea
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.alennus <= ?1 AND o.name NOT LIKE %?2% ORDER BY o.alennus ASC")
    List<OutletTuote> activeSearcAlennusAscNoApple(double ale, String haku);
    
    //aktiiviset alennuksen mukaan asc
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.alennus <= ?1 ORDER BY o.alennus ASC")
    List<OutletTuote> activeSearcAlennusAsc(double ale);
    
    //aktiiviset alennuksen mukaan desc
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.alennus <= ?1 ORDER BY o.alennus DESC")
    List<OutletTuote> activeSearcAlennusDes(double ale);
    
    //kaikki paivaa ennen, vanhin ylimpana
    @Query("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.priceUpdatedDate <= ?1 ORDER BY o.priceUpdatedDate ASC")
    List<OutletTuote> activeListAllSortChangeDateAsc(LocalDate date);
    
    //kaikki paivaa ennen, vanhin alimpana
    @Query("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.priceUpdatedDate <= ?1 ORDER BY o.priceUpdatedDate DESC")
    List<OutletTuote> activeListAllSortChangeDateDesc(LocalDate date);
    
    //kaikki aktiiviset outID:n mukaan asc
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL ORDER BY o.outId ASC")
    List<OutletTuote> acticeSearchAllSortOutIdAsc();
    
    //kaikki aktiiviset outID:n mukaan desc
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL ORDER BY o.outId DESC")
    List<OutletTuote> activeSearchAllSortOutIdDes();
    
    //kaikki aktiiviset hinnan mukaan asc, max hinta
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.outPrice <= ?1 ORDER BY o.outPrice ASC")
    List<OutletTuote> activeSearchAllSortOutHintaAsc(double hinta);
    
    //kaikki aktiiviset hinnan mukaan desc, max hinta
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.outPrice <= ?1 ORDER BY o.outPrice DESC")
    List<OutletTuote> activeSearchAllSortOutHintaDes(double hinta);
    
    //aktiiviset (vk sivulla) tuotteet, aleprossan mukaan, asc
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.poistotuote IS FALSE ORDER BY o.alennus ASC")
    List<OutletTuote> activeListAllVkActive(boolean poisto);
    
    //dumppituotteet aleprossan mukaan, asc
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.dumppituote IS TRUE ORDER BY o.alennus ASC")
    List<OutletTuote> activeListAllDumppi(boolean dumppi);
    
    //poistetut PID:n mukaan asc
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NOT NULL ORDER BY o.pid DESC")
    List<OutletTuote> deletedListAllByPpid();
    
    //poistetut haku nimen mukaan, sorttaus PID mukaan
    List<OutletTuote> findByNameLikeIgnoreCaseAndDeletedIsNotNullOrderByNameAsc(String haku);
    
    //poistetut poistopaivan mukaan desc
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NOT NULL ORDER BY o.deleted DESC")
    List<OutletTuote> deletedListAllByDelDate();
    
    //poistetut outId mukaan asc
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NOT NULL ORDER BY o.outId ASC")
    List<OutletTuote> deletedListAllByOutId();
    
    //poistetut tietylta paivalta
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NOT NULL AND o.deleted IS ?1 ORDER BY o.pid DESC")
    List<OutletTuote> deletedListByDate(LocalDate date);
    
    //aktiivinen pid
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.pid IS ?1 ORDER BY o.outId ASC")
    List<OutletTuote> activeByPid(int pid);
    
    //poistunut pid
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NOT NULL AND o.pid IS ?1 ORDER BY o.outId ASC")
    List<OutletTuote> deletedByPid(int pid);
    
    public List<OutletTuote> findByDeletedIsNullOrderByAlennusAsc();
    public List<OutletTuote> findByDeletedNotNull();
    public List<OutletTuote> findByDeletedIsNull();
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

    
    
}
