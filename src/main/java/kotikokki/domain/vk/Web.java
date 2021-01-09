/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain.vk;

public class Web {
    private transient String stockStatus;
    private transient int minDays;
    private transient int maxDays;
    private transient boolean isAvailable;
    private transient boolean isPurchasable;
    private int stock;
    private transient String ranking;
    private transient Warehouses warehouses;

    public Web(String stockStatus, int minDays, int maxDays, boolean isAvailable, boolean isPurchasable, int stock, String ranking, Warehouses warehouses) {
        this.stockStatus = stockStatus;
        this.minDays = minDays;
        this.maxDays = maxDays;
        this.isAvailable = isAvailable;
        this.isPurchasable = isPurchasable;
        this.stock = stock;
        this.ranking = ranking;
        this.warehouses = warehouses;
    }

    
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public int getMinDays() {
        return minDays;
    }

    public void setMinDays(int minDays) {
        this.minDays = minDays;
    }

    public int getMaxDays() {
        return maxDays;
    }

    public void setMaxDays(int maxDays) {
        this.maxDays = maxDays;
    }

    public boolean isIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isIsPurchasable() {
        return isPurchasable;
    }

    public void setIsPurchasable(boolean isPurchasable) {
        this.isPurchasable = isPurchasable;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public Warehouses getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(Warehouses warehouses) {
        this.warehouses = warehouses;
    }
    
    
    
}
