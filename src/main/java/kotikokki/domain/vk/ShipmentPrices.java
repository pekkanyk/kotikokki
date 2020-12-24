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
class ShipmentPrices {
    private int id;
    private boolean pickup;
    private double price;
    private String name;

    public ShipmentPrices(int id, boolean pickup, double price, String name) {
        this.id = id;
        this.pickup = pickup;
        this.price = price;
        this.name = name;
    }
    
    
    
}
