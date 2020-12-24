/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain.vk;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author qru19
 */
@Data
@AllArgsConstructor
public class Tuotteet {
    private String provider;
    private int totalItems;
    private int pageNo;
    private int numPages;
    private List<Product> products;
    private transient List<Facets> facets;


    public int getNumPages() {
        return numPages;
    }
    
    public List<Product> getProducts(){
        return this.products;
    }
    
    
}

