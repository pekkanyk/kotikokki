/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pekka
 */
@Service
public class ListaService {
        
    public List<String> listaa(String paikka, String lista_cp){
        List<String> lista = new ArrayList();
        //String[] rivit = lista_cp.split(System.getProperty("line.separator"));
        Scanner scanner = new Scanner(lista_cp);
        while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String rivi = "";
        String[] rivi_array = line.split("\t");
        if (rivi_array.length==7){
            
            if (paikka.length()>1){
                if (rivi_array[4].length()>1 || rivi_array[3].contains("Matka")) rivi=rivi_array[5];
                if (!rivi.equals("")) {
                    rivi=rivi_array[4]+" "+ rivi;
                    if (rivi_array[3].contains("Matka")) rivi=rivi+ ", MATKAHUOLTO";
                    if (!rivi_array[2].equals("Ei")) rivi=rivi+ ", HENKSU";
                lista.add(rivi);
            }
            }
            else {
            if (rivi_array[4].equals(paikka) && !rivi_array[3].contains("Matka")) rivi = rivi_array[5];
            
            if (!rivi.equals("")) {
                if (!rivi_array[2].equals("Ei")) rivi="H "+rivi;
                else rivi="o "+rivi;
                lista.add(rivi);
            }
            }    
       
        }
        }
        scanner.close();
        Collections.reverse(lista);
        List<String> lista_viivoilla = new ArrayList();
        int counter = 1;
        //lista_viivoilla.add("********** "+paikka+" **********");
        for (String rivi:lista){
            if (lista_viivoilla.size()==(10*counter+(counter-1))){
                lista_viivoilla.add("----------"+10*counter+"----------");
                counter++;
            }
            lista_viivoilla.add(rivi);
        } 
        return lista_viivoilla;
    }
    
    public List<String> sivut(String lista_cp){
        List<String> sivutettu_lista = new ArrayList();
        String[] kerayspaikat = {"1","2","3","4","5","6","7","8","9","0","MUUT / ISOT & MATKAHUOLTO"};
        int rivimaara=0;
        for (int i=0; i<kerayspaikat.length;i++){
            
            sivutettu_lista.add("********** "+kerayspaikat[i]+" **********");
            rivimaara++;
            for (String rivi:this.listaa(kerayspaikat[i], lista_cp)){
                sivutettu_lista.add(rivi);
                rivimaara++;
                if (rivimaara >=52) rivimaara=0;
            }
            sivutettu_lista.add("***********************");
            sivutettu_lista.add(".");
            sivutettu_lista.add(".");
            sivutettu_lista.add(".");
            rivimaara=rivimaara+4;
            if (rivimaara>=21){
                for (int j=0;j<52-rivimaara;j++) {
                    sivutettu_lista.add(".");
                }
                rivimaara=0;
            }
        }
        
        return sivutettu_lista;
    }
    
}