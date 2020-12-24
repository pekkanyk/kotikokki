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
class Category {
    private String id;
    private Path[] path;
    private String slug;
    private String name;

    public Category(String id, Path[] path, String slug, String name) {
        this.id = id;
        this.path = path;
        this.slug = slug;
        this.name = name;
    }
    
    
    
}
