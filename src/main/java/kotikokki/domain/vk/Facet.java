/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain.vk;

/**
 *
 * @author qru19
 */
class Facet {
    private String value;
    private String title;
    private int productCount;

    public Facet(String value, String title, int productCount) {
        this.value = value;
        this.title = title;
        this.productCount = productCount;
    }
    
    
}