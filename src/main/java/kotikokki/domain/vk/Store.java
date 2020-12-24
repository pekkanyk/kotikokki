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
class Store {
    private String stockStatus;
    private int minDays;
    private int maxDays;
    private boolean isAvailable;
    private boolean isPurchasable;
    private int stock;

    public Store(String stockStatus, int minDays, int maxDays, boolean isAvailable, boolean isPurchasable, int stock) {
        this.stockStatus = stockStatus;
        this.minDays = minDays;
        this.maxDays = maxDays;
        this.isAvailable = isAvailable;
        this.isPurchasable = isPurchasable;
        this.stock = stock;
    }
    
    
    
}
