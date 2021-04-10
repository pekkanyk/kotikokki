/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain;

import java.time.LocalDate;

/**
 *
 * @author qru19
 */
public class PvmStatistiikka {
    private LocalDate pvm;
    private long count;
    private double ale;
    private double myyntipvka;
    private long count2;

    public PvmStatistiikka() {
        this.pvm = LocalDate.parse("1970-01-01");
        this.count = 0;
        this.ale = 0.0;
        this.myyntipvka = 0.0;
        this.count2 = 0;
    }
    
    
    public PvmStatistiikka(LocalDate pvm, long count, double ale, double myyntipvka){
        this.pvm=pvm;
        this.count=count;
        this.ale = ale;
        this.myyntipvka = myyntipvka;
        this.count2 = 0;
    }
    public PvmStatistiikka(LocalDate pvm, long count, double ale, double myyntipvka, long count2){
        this.pvm=pvm;
        this.count=count;
        this.ale = ale;
        this.myyntipvka = myyntipvka;
        this.count2 = count2;
    }

    public long getCount2() {
        return count2;
    }
    
    
    public LocalDate getPvm() {
        return pvm;
    }

    public long getCount() {
        return count;
    }

    public double getAle() {
        return ale;
    }

    public double getMyyntipvka() {
        return myyntipvka;
    }
    public String dateStr(){
        return pvm.toString();
    }

    
    
    
}
