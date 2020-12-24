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
class Brand {
    private String support;
    private int id;
    private String url;
    private String slug;
    private String name;

    public Brand(String support, int id, String url, String slug, String name) {
        this.support = support;
        this.id = id;
        this.url = url;
        this.slug = slug;
        this.name = name;
    }
    
    
}
