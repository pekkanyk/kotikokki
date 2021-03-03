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
import org.springframework.data.domain.Sort;
import kotikokki.domain.OutletTuote;

/**
 *
 * @author Pekka
 */
public interface OutletTuoteRepository extends JpaRepository<OutletTuote, Long>{
    //haut nimen perusteella
    //@Query ("SELECT o FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) ORDER BY o.pid, o.deleted DESC, o.priceUpdatedDate DESC")
    @Query ("SELECT o FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND (o.priceUpdatedDate>=?2 OR o.deleted>=?2) AND (o.priceUpdatedDate<=?3 OR o.deleted<=?3)")
    List<OutletTuote> haeNimellaKaikistaAlkaenAsti(String haku, LocalDate alkaen, LocalDate asti, Sort sort); 
    @Query ("SELECT o FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND o.deleted IS NULL AND o.priceUpdatedDate>=?2 AND o.priceUpdatedDate<=?3")
    List<OutletTuote> haeNimellaAktiivisistaAlkaenAsti(String haku, LocalDate alkaen, LocalDate asti, Sort sort);
    @Query ("SELECT o FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND o.deleted IS NOT NULL AND o.deleted>=?2 AND o.deleted<=?3")
    List<OutletTuote> haeNimellaPoistuneistaAlkaenAsti(String haku, LocalDate alkaen, LocalDate asti, Sort sort);
    
    
    //haut PID perusteella
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.pid IS ?1 ORDER BY o.priceUpdatedDate DESC")
    List<OutletTuote> activeByPid(int pid);
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.pid IS ?1")
    List<OutletTuote> activeByPidTEST(int pid,Sort sort);
    @Query ("SELECT o FROM OutletTuote o WHERE o.pid IS ?1 ORDER BY o.deleted DESC,o.priceUpdatedDate DESC")
    List<OutletTuote> allbyPid(int pid);
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NOT NULL AND o.pid IS ?1 ORDER BY o.deleted DESC,o.priceUpdatedDate DESC")
    List<OutletTuote> deletedByPid(int pid);
    
    
    //haut keskiarvoalea varten
    @Query ("SELECT SUM (o.outPrice) FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND o.deleted IS NOT NULL AND o.deleted>=?2 AND o.deleted<=?3")
    Double haeNimellaSummaaOutPriceDeleted(String haku,LocalDate alkaen, LocalDate asti);
    @Query("SELECT SUM(o.norPrice) FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND o.deleted IS NOT NULL AND o.deleted>=?2 AND o.deleted<=?3")
    Double haeNimellaSummaaNorPriceDeleted(String haku,LocalDate alkaen, LocalDate asti);
    @Query ("SELECT SUM (o.outPrice) FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND o.deleted IS NULL AND o.priceUpdatedDate>=?2 AND o.priceUpdatedDate<=?3")
    Double haeNimellaSummaaOutPriceActive(String haku,LocalDate alkaen, LocalDate asti);
    @Query("SELECT SUM(o.norPrice) FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND o.deleted IS NULL AND o.priceUpdatedDate>=?2 AND o.priceUpdatedDate<=?3")
    Double haeNimellaSummaaNorPriceActive(String haku,LocalDate alkaen, LocalDate asti);
    @Query ("SELECT SUM (o.outPrice) FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND (o.priceUpdatedDate>=?2 OR o.deleted>=?2) AND (o.priceUpdatedDate<=?3 OR o.deleted<=?3)")
    Double haeNimellaSummaaOutPriceBoth(String haku,LocalDate alkaen, LocalDate asti);
    @Query("SELECT SUM(o.norPrice) FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND (o.priceUpdatedDate>=?2 OR o.deleted>=?2) AND (o.priceUpdatedDate<=?3 OR o.deleted<=?3)")
    Double haeNimellaSummaaNorPriceBoth(String haku,LocalDate alkaen, LocalDate asti);
    
    @Query ("SELECT SUM (o.outPrice) FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND o.deleted IS NOT NULL AND o.deleted>=?2 AND o.deleted<=?3 AND o.condition=?4")
    Double haeNimellaSummaaOutPriceDeletedKl(String haku,LocalDate alkaen, LocalDate asti, String condition);
    @Query("SELECT SUM(o.norPrice) FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND o.deleted IS NOT NULL AND o.deleted>=?2 AND o.deleted<=?3 AND o.condition=?4")
    Double haeNimellaSummaaNorPriceDeletedKl(String haku,LocalDate alkaen, LocalDate asti, String condition);
    @Query ("SELECT SUM (o.outPrice) FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND o.deleted IS NULL AND o.priceUpdatedDate>=?2 AND o.priceUpdatedDate<=?3 AND o.condition=?4")
    Double haeNimellaSummaaOutPriceActiveKl(String haku,LocalDate alkaen, LocalDate asti, String condition);
    @Query("SELECT SUM(o.norPrice) FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND o.deleted IS NULL AND o.priceUpdatedDate>=?2 AND o.priceUpdatedDate<=?3 AND o.condition=?4")
    Double haeNimellaSummaaNorPriceActiveKl(String haku,LocalDate alkaen, LocalDate asti, String condition);
    @Query ("SELECT SUM (o.outPrice) FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND (o.priceUpdatedDate>=?2 OR o.deleted>=?2) AND (o.priceUpdatedDate<=?3 OR o.deleted<=?3) AND o.condition=?4")
    Double haeNimellaSummaaOutPriceBothKl(String haku,LocalDate alkaen, LocalDate asti, String condition);
    @Query("SELECT SUM(o.norPrice) FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND (o.priceUpdatedDate>=?2 OR o.deleted>=?2) AND (o.priceUpdatedDate<=?3 OR o.deleted<=?3) AND o.condition=?4")
    Double haeNimellaSummaaNorPriceBothKl(String haku,LocalDate alkaen, LocalDate asti, String condition);
    
    
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
    List<OutletTuote> findByNameLikeIgnoreCaseOrderByNameAsc(String haku);
    @Query ("SELECT o FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) ORDER BY o.pid, o.deleted DESC, o.priceUpdatedDate DESC")
    List<OutletTuote> searchByName(String haku);
    
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
    @Query("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.priceUpdatedDate <= ?1 ORDER BY o.priceUpdatedDate ASC, o.outId ASC")
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
    
    //aktiiviset (vk sivulla) tuotteet, pvm mukaan, asc
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.poistotuote IS FALSE ORDER BY o.priceUpdatedDate ASC, o.pid ASC")
    List<OutletTuote> activeListAllVkActive(boolean poisto);
    
    //dumppituotteet pvm mukaan, asc
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.dumppituote IS TRUE ORDER BY o.priceUpdatedDate ASC, o.pid ASC")
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
    
    
    
    //aktiivinen yli x pv vanha ja yli x e hintainen, vanhin ensin
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.priceUpdatedDate <= ?1 AND o.outPrice >= ?2 ORDER BY o.priceUpdatedDate ASC, o.pid ASC,o.outId ASC")
    List<OutletTuote> ennenYliHinnan(LocalDate date, double outHinta);
    
    //aktiivisten tuotteiden pvm + lkm
    @Query("SELECT DISTINCT o.priceUpdatedDate FROM OutletTuote o WHERE o.deleted IS NULL ORDER BY o.priceUpdatedDate DESC")
    List<LocalDate> activePaivamaarat();
    @Query ("SELECT COUNT (o) FROM OutletTuote o WHERE o.deleted IS NULL AND o.priceUpdatedDate=?1")
    long activeTuotemaaraPvm(LocalDate date);
    
    //poistettujen tuotteiden pvm + lkm
    @Query("SELECT DISTINCT o.deleted FROM OutletTuote o WHERE o.deleted IS NOT NULL AND o.deleted > (CURRENT_DATE - 30) ORDER BY o.deleted DESC")
    List<LocalDate> deletedPaivamaarat();
    @Query ("SELECT COUNT (o) FROM OutletTuote o WHERE o.deleted=?1")
    long deletedTuotemaaraPvm(LocalDate date);
    
    //aktiivisten eri tuotteiden lukumaara
    @Query ("SELECT COUNT (DISTINCT o.pid) FROM OutletTuote o WHERE o.deleted IS NULL")
    long activeDistinctProducts();
    //aktiivisten tuotteiden pid:t
    @Query ("SELECT DISTINCT o.pid FROM OutletTuote o WHERE o.deleted IS NULL")
    List<Integer> activeDistinctProductPids();
    
    //kuinka monta samalla pid:lla olevaa aktiivista on
    @Query ("SELECT COUNT (o) FROM OutletTuote o WHERE o.pid=?1 AND o.deleted IS NULL")
    int countPid(int pid);
    //kaikenkaikkiaan, myos poistetut
    @Query ("SELECT COUNT (o) FROM OutletTuote o WHERE o.pid=?1")
    int countPidAll(int pid);
    //ja poistetut, yli pvm sitten
    @Query ("SELECT COUNT (o) FROM OutletTuote o WHERE o.pid=?1 AND o.deleted IS NOT NULL")
    int countPidDeleted(int pid);
    
    //keskiarvo alennuksen laskua
    @Query ("SELECT SUM (o.outPrice) FROM OutletTuote o WHERE o.deleted IS NULL")
    double activeSumOutPrice();
    @Query ("SELECT SUM (o.norPrice) FROM OutletTuote o WHERE o.deleted IS NULL")
    double activeSumNorPrice();
    @Query ("SELECT SUM (o.outPrice) FROM OutletTuote o WHERE o.deleted IS NOT NULL")
    double deletedSumOutPrice();
    @Query ("SELECT SUM (o.norPrice) FROM OutletTuote o WHERE o.deleted IS NOT NULL")
    double deletedSumNorPrice();
    
    //keskiarvo alennuksen laskua, pvm
    @Query ("SELECT SUM (o.outPrice) FROM OutletTuote o WHERE o.deleted IS NULL AND o.priceUpdatedDate =?1")
    double activeSumOutPricePvm(LocalDate date);
    @Query ("SELECT SUM (o.norPrice) FROM OutletTuote o WHERE o.deleted IS NULL AND o.priceUpdatedDate =?1")
    double activeSumNorPricePvm(LocalDate date);
    @Query ("SELECT SUM (o.outPrice) FROM OutletTuote o WHERE o.deleted =?1")
    double deletedSumOutPricePvm(LocalDate date);
    @Query ("SELECT SUM (o.norPrice) FROM OutletTuote o WHERE o.deleted =?1")
    double deletedSumNorPricePvm(LocalDate date);
    
    //paivia hinnanmuutoksen ja poistumisen valilla
    @Query (value= "SELECT AVG(deleted - price_updated_date) FROM outlet_tuote WHERE deleted=?1", nativeQuery = true)
    double deletedMyyntiPv(LocalDate date);
    //lisayspvm
    @Query("SELECT DISTINCT o.firstSeen FROM OutletTuote o WHERE o.firstSeen IS NOT NULL AND o.firstSeen > (CURRENT_DATE -30)ORDER BY o.firstSeen DESC")
    List<LocalDate> lisaysPaivamaarat();
    @Query ("SELECT COUNT (o) FROM OutletTuote o WHERE o.firstSeen=?1")
    long lisaysTuotemaaraPvm(LocalDate date);
    
    //paivia lisayksen ja poistumisen valilla
    @Query (value= "SELECT AVG(deleted - first_seen) FROM outlet_tuote WHERE first_seen=?1 AND deleted IS NOT NULL", nativeQuery = true)
    double deletedLisaysPv(LocalDate date); 
    //kaikkien poistettujen yhteenlaskettu keskiarvopoistumisaika 7.1.2021 jalkeen (vaaristaa tilastoja, silloin seuranta aloitettu)
    @Query (value= "SELECT AVG(deleted - first_seen) FROM outlet_tuote WHERE first_seen>='2021-01-07' AND deleted IS NOT NULL", nativeQuery = true)
    double deletedLisaysPvKaikkiKa();
    @Query ("SELECT COUNT (o) FROM OutletTuote o WHERE o.firstSeen=?1 AND o.deleted IS NOT NULL")
    long lisaysPoistuneetPvm(LocalDate date);
    @Query ("SELECT COUNT (o) FROM OutletTuote o WHERE o.firstSeen=?1 AND o.deleted IS NULL")
    long lisaysAktiivisetPvm(LocalDate date);
    
    @Query (value= "SELECT SUM(deleted - first_seen) FROM outlet_tuote WHERE first_seen=?1 AND deleted IS NOT NULL", nativeQuery = true)
    long deletedLisaysPvKpl(LocalDate date); 
    
    @Query (value= "SELECT SUM(CURRENT_DATE - first_seen) FROM outlet_tuote WHERE first_seen=?1 AND deleted IS NULL", nativeQuery = true)
    long activeLisaysPvKpl(LocalDate date);
    
    
    //pvm kikkailu, jotta statsit oikeammat.
    @Query (value= "SELECT SUM(CURRENT_DATE - first_seen) FROM outlet_tuote WHERE deleted IS NULL AND first_seen>='2021-01-07'", nativeQuery = true)
    long activeDays();
    @Query (value= "SELECT SUM(deleted - first_seen) FROM outlet_tuote WHERE deleted IS NOT NULL AND first_seen>='2021-01-07'", nativeQuery = true)
    long deletedDays();
    @Query ("SELECT COUNT(o) FROM OutletTuote o WHERE firstSeen>='2021-01-07'")
    long countAll();
    
    //@Query ("SELECT o FROM OutletTuote o WHERE o.pid IN (SELECT DISTINCT o.pid FROM OutletTuote o WHERE o.deleted IS NULL) ORDER BY o.pid ASC")
    //List<OutletTuote> yliXaktiivista(long x);
    
    @Query ("SELECT o FROM OutletTuote o WHERE o.deleted IS NULL AND o.priceUpdatedDate = ?1 ORDER BY o.firstSeen ASC")
    List<OutletTuote> activeForDate(LocalDate date);
    @Query ("SELECT o FROM OutletTuote o WHERE o.firstSeen = ?1 ORDER BY o.pid ASC")
    List<OutletTuote> firstSeenForDate(LocalDate date);
    
    @Query ("SELECT o FROM OutletTuote o WHERE o.dumppituote=true AND o.onVarasto=true AND o.deleted IS NULL ORDER BY o.pid ASC")
    List<OutletTuote> activeDumppiVarastolla();
    
    @Query(value= "SELECT COUNT(*) FROM outlet_tuote o WHERE RIGHT(CAST(out_id AS Varchar(255)) ,1)=?1 AND deleted IS NULL", nativeQuery = true)
    Long countLastNumberOfOutIdIs(String nro);
    @Query(value= "SELECT COUNT(*) FROM outlet_tuote o WHERE RIGHT(CAST(out_id AS Varchar(255)) ,1)=?1 AND deleted IS NULL AND o.koko=?2", nativeQuery = true)
    Integer countLastNumberOfOutIdIsSize(String nro,String koko);
    
    //historiaan liittyvia
    @Query ("SELECT SUM (o.outPrice) FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND o.name NOT LIKE '%lle%' AND o.deleted>=?2 AND o.deleted<=?3")
    Double summaaOutPriceDeleted(String haku,LocalDate asti, LocalDate alkaen);
    @Query("SELECT SUM(o.norPrice) FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND o.name NOT LIKE '%lle%' AND o.deleted>=?2 AND o.deleted<=?3")
    Double summaaNorPriceDeleted(String haku,LocalDate asti, LocalDate alkaen);
    @Query("SELECT o FROM OutletTuote o WHERE LOWER(o.name) LIKE LOWER(?1) AND o.name NOT LIKE '%lle%' AND o.deleted>=?2 AND o.deleted<=?3 ORDER BY o.deleted ASC, o.name ASC")
    List<OutletTuote> deletedSearchNameWithDate(String haku,LocalDate asti, LocalDate alkaen);
    
    //poistuneiden tuotteiden aleprosentteihin liityvia
    @Query("SELECT DISTINCT o.condition FROM OutletTuote o WHERE o.deleted IS NOT NULL ORDER BY o.condition")
    List<String> kuntoluokat();
    @Query ("SELECT SUM (o.outPrice) FROM OutletTuote o WHERE o.condition=?1 AND o.deleted IS NOT NULL")
    Double summaaPoistuneetOutPriceKuntoluokalla(String kuntoluokka);
    @Query ("SELECT SUM (o.norPrice) FROM OutletTuote o WHERE o.condition=?1 AND o.deleted IS NOT NULL")
    Double summaaPoistuneetNorPriceKuntoluokalla(String kuntoluokka);
    
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
    public void deleteByUpdated(boolean updated);
    public List<OutletTuote> findByUpdated(boolean updated);
    
    
}
