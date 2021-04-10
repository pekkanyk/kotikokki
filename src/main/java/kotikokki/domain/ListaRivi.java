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
public class ListaRivi implements Comparable<ListaRivi>{
    private int outId;
    private String rivi;

    public ListaRivi() {
    }

    public ListaRivi(int outId, String rivi) {
        this.outId = outId;
        this.rivi = rivi;
    }

    public int getOutId() {
        return outId;
    }

    public void setOutId(int outId) {
        this.outId = outId;
    }

    public String getRivi() {
        return rivi;
    }

    public void setRivi(String rivi) {
        this.rivi = rivi;
    }

    public int compareTo(ListaRivi o) {
        String tama = String.valueOf(this.outId);
        String toinen = String.valueOf(o.outId);
        int tamaLast2 = Integer.valueOf(tama.substring(tama.length()-2));
        int toinenLast2 = Integer.valueOf(toinen.substring(toinen.length()-2));
        return tamaLast2 - toinenLast2;
        //return this.outId - o.outId;
    }
    
    
}
