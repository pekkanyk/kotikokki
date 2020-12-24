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
class Restrictions {
    private String description;
    private boolean isRestricted;
    private String descriptionShort;
    private String[] pickupAllowed;
    private String[] postalCodeAllowed;

    public Restrictions(String description, boolean isRestricted, String descriptionShort, String[] pickupAllowed, String[] postalCodeAllowed) {
        this.description = description;
        this.isRestricted = isRestricted;
        this.descriptionShort = descriptionShort;
        this.pickupAllowed = pickupAllowed;
        this.postalCodeAllowed = postalCodeAllowed;
    }
    
    
    
    
}
