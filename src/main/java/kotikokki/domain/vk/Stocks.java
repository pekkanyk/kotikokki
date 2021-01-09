/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain.vk;

public class Stocks {
    private Web web;
    private transient Stores stores;

    public Stocks(Web web, Stores stores) {
        this.web = web;
        this.stores = stores;
    }
    

    public Web getWeb() {
        return web;
    }

    public void setWeb(Web web) {
        this.web = web;
    }

    public Stores getStores() {
        return stores;
    }

    public void setStores(Stores stores) {
        this.stores = stores;
    }
    
    
    
}
