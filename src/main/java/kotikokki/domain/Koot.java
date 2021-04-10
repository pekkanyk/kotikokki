/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain;

/**
 *
 * @author qru19
 */
public class Koot {
    private int hylly;
    private int pieni;
    private int keski;
    private int iso;

    public Koot() {
        hylly = 0;
        pieni = 0;
        keski = 0;
        iso = 0;
    }

    public void setHylly(int hylly) {
        this.hylly = hylly;
    }

    public void setPieni(int pieni) {
        this.pieni = pieni;
    }

    public void setKeski(int keski) {
        this.keski = keski;
    }

    public void setIso(int iso) {
        this.iso = iso;
    }
    
    @Override
    public String toString(){
        int total = pieni+keski+iso;
        double pP =0.0;
        double pK =0.0;
        double pI =0.0;
        if (total!=0) {
            pP= (pieni/(1.0*total)*100);
            pK= (keski/(1.0*total)*100);
            pI= (iso/(1.0*total)*100);
        }
        return hylly+": "+total+" "+
                String.format("%.1f", pP)+"/"+
                String.format("%.1f", pK)+"/"+
                String.format("%.1f", pI);
                
    }
}
