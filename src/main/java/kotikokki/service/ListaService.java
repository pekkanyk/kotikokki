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
import kotikokki.domain.ListaRivi;
import kotikokki.domain.OutletTuote;
import kotikokki.domain.PidPackage;
import kotikokki.repository.OutletTuoteRepository;
import kotikokki.repository.PidPackageRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Pekka
 */
@Service
public class ListaService {
    @Autowired
    private OutletTuoteRepository outletTuoteRepository;
    @Autowired
    private PidPackageRepository pidPackageRepository;
        
    public List<String> listaa(String paikka, String lista_cp){
        //List<String> lista = new ArrayList();
        List<ListaRivi> lista = new ArrayList();
        //String[] rivit = lista_cp.split(System.getProperty("line.separator"));
        Scanner scanner = new Scanner(lista_cp);
        while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String rivi = "";
        String[] rivi_array = line.split("\t");
        if (rivi_array.length==8){
            
            if (paikka.length()>1){
                if (rivi_array[4].length()>1 || rivi_array[3].contains("Matka")) rivi=rivi_array[6];
                if (!rivi.equals("")) {
                    rivi=rivi_array[4]+" "+ rivi;
                    if (rivi_array[3].contains("Matka")) rivi=rivi+ ", MATKAHUOLTO";
                    if (!rivi_array[2].equals("Ei")) rivi=rivi+ ", HENKSU";
                    String[] riviOsina = rivi_array[6].split(" ");
                    String outId = riviOsina[0].substring(3).trim();
                //lista.add(rivi);
                lista.add(new ListaRivi(Integer.valueOf(outId),rivi));
            }
            }
            else {
            
            String[] riviOsina = rivi_array[6].split(" ");
            String outId = riviOsina[0].substring(3).trim();
            String til = "";
            String daysActive = "";
            int tilavuus = 0;
            int pid = 0;
            if (rivi_array[4].equals(paikka) && !rivi_array[3].contains("Matka")) {
                rivi = rivi_array[6]+" <-CHECK ID";
            //}
            PidPackage tiedot = new PidPackage();
            
            if (outletTuoteRepository.findByOutId(Integer.valueOf(outId))!=null){
                OutletTuote outTuote = outletTuoteRepository.findByOutId(Integer.valueOf(outId));
                //pid = outletTuoteRepository.findByOutId(Integer.valueOf(outId)).getPid();
                //String tuote = outletTuoteRepository.findByOutId(Integer.valueOf(outId)).getName();
                pid = outTuote.getPid();
                String tuote = outTuote.getName();
                daysActive = outTuote.daysActiveDeleted();
                if (tuote.length()>67) rivi = "OUT"+outId+" ("+tuote.substring(0,67)+")";
                else rivi = "OUT"+outId+" ("+tuote+")";
            }
            if (pid!=0){
                if(pidPackageRepository.findByPid(pid)!=null){
                    tiedot  = pidPackageRepository.findByPid(pid);
                    til = tiedot.getWidth()/10+"x"+tiedot.getHeight()/10+"x"+tiedot.getDepth()/10;
                }
            }
            }
            //if (!til.equals("")){
            //    rivi = "OUT"+outId+" ("+outletTuoteRepository.findByOutId(Integer.valueOf(outId)).getName()+")";
            //}
            
            if (!rivi.equals("")) {
                if (!rivi_array[2].equals("Ei")) rivi="H "+rivi+" "+til+" "+daysActive;
                else rivi="o "+rivi+" "+til+" "+daysActive;
                //lista.add(rivi);
                lista.add(new ListaRivi(Integer.valueOf(outId),rivi));
            }
            }    
       
        }
        }
        scanner.close();
        //Collections.reverse(lista);
        /*for (ListaRivi row:lista){
            String outAsString = String.valueOf(row.getOutId());
            String outLast2 = outAsString.substring(outAsString.length()-2);
            row.setOutId(Integer.valueOf(outLast2));
        }
        */
        Collections.sort(lista);
        
        List<String>stringLista = new ArrayList();
        for (ListaRivi row:lista){
            stringLista.add(row.getRivi());
        }
        /*
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
        */
        //return lista_viivoilla;
        return stringLista;
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
    public List<String> etsiPiilotetut(String lista_cp){
        List<String> loydetyt = new ArrayList();
        Scanner scanner = new Scanner(lista_cp);
        while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] rivi_array = line.split("\t");
        int id=0;
        if (isNumeric(rivi_array[0])){
            id = Integer.valueOf(rivi_array[0].trim());
        }
        
        if (outletTuoteRepository.findByOutIdAndDeletedIsNull(id)==null && id!=0){
            loydetyt.add(String.valueOf(id));
        }
        
        }
        scanner.close();
        
        return loydetyt;
    }
    
    private static boolean isNumeric(String strNum) {
    if (strNum == null) {
        return false;
    }
    try {
        double d = Double.parseDouble(strNum);
    } catch (NumberFormatException nfe) {
        return false;
    }
    return true;
}
    
}
