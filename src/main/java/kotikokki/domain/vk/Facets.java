/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain.vk;

import java.util.List;

/**
 *
 * @author qru19
 */
public class Facets {
    private String id;
    private String title;
    private List<Facet> values;

    public Facets(String id, String title, List<Facet> values) {
        this.id = id;
        this.title = title;
        this.values = values;
    }
    
    
}
