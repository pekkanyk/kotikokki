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

public class Availability {
    private transient String _id;
    private transient boolean isPickupAllowed;
    private boolean hasStock;
    private transient boolean isInStoresOnly;
    private boolean isEOL;
    private transient boolean isFastDeliveryAvailable;
    private transient int updateCount;
    private transient boolean isReleased;
    private transient boolean isElectronic;
    private Stocks stocks;
    private transient String verifiedAt;
    private transient boolean isStockVisible;
    private transient boolean isShippingAllowed;
    private transient boolean isStoreOrderAllowed;
    private transient String overrideText;
    private transient boolean isSoldOut;
    private transient String updatedAt;
    private boolean isPurchasable;
    private transient int pid;
    private transient String[] tags;
    private transient boolean isVirtual;
    private transient String updateStartTime;

    public Availability(String _id, boolean isPickupAllowed, boolean hasStock, boolean isInStoresOnly, boolean isEOL, boolean isFastDeliveryAvailable, int updateCount, boolean isReleased, boolean isElectronic, Stocks stocks, String verifiedAt, boolean isStockVisible, boolean isShippingAllowed, boolean isStoreOrderAllowed, String overrideText, boolean isSoldOut, String updatedAt, boolean isPurchasable, int pid, String[] tags, boolean isVirtual, String updateStartTime) {
        this._id = _id;
        this.isPickupAllowed = isPickupAllowed;
        this.hasStock = hasStock;
        this.isInStoresOnly = isInStoresOnly;
        this.isEOL = isEOL;
        this.isFastDeliveryAvailable = isFastDeliveryAvailable;
        this.updateCount = updateCount;
        this.isReleased = isReleased;
        this.isElectronic = isElectronic;
        this.stocks = stocks;
        this.verifiedAt = verifiedAt;
        this.isStockVisible = isStockVisible;
        this.isShippingAllowed = isShippingAllowed;
        this.isStoreOrderAllowed = isStoreOrderAllowed;
        this.overrideText = overrideText;
        this.isSoldOut = isSoldOut;
        this.updatedAt = updatedAt;
        this.isPurchasable = isPurchasable;
        this.pid = pid;
        this.tags = tags;
        this.isVirtual = isVirtual;
        this.updateStartTime = updateStartTime;
    }
    
    

    public boolean isIsEOL() {
        return isEOL;
    }

    public Stocks getStocks() {
        return stocks;
    }

    public void setIsEOL(boolean isEOL) {
        this.isEOL = isEOL;
    }

    public void setStocks(Stocks stocks) {
        this.stocks = stocks;
    }

    public boolean isHasStock() {
        return hasStock;
    }

    public void setHasStock(boolean hasStock) {
        this.hasStock = hasStock;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public boolean isIsPickupAllowed() {
        return isPickupAllowed;
    }

    public void setIsPickupAllowed(boolean isPickupAllowed) {
        this.isPickupAllowed = isPickupAllowed;
    }

    public boolean isIsInStoresOnly() {
        return isInStoresOnly;
    }

    public void setIsInStoresOnly(boolean isInStoresOnly) {
        this.isInStoresOnly = isInStoresOnly;
    }

    public boolean isIsFastDeliveryAvailable() {
        return isFastDeliveryAvailable;
    }

    public void setIsFastDeliveryAvailable(boolean isFastDeliveryAvailable) {
        this.isFastDeliveryAvailable = isFastDeliveryAvailable;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public boolean isIsReleased() {
        return isReleased;
    }

    public void setIsReleased(boolean isReleased) {
        this.isReleased = isReleased;
    }

    public boolean isIsElectronic() {
        return isElectronic;
    }

    public void setIsElectronic(boolean isElectronic) {
        this.isElectronic = isElectronic;
    }

    public String getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(String verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public boolean isIsStockVisible() {
        return isStockVisible;
    }

    public void setIsStockVisible(boolean isStockVisible) {
        this.isStockVisible = isStockVisible;
    }

    public boolean isIsShippingAllowed() {
        return isShippingAllowed;
    }

    public void setIsShippingAllowed(boolean isShippingAllowed) {
        this.isShippingAllowed = isShippingAllowed;
    }

    public boolean isIsStoreOrderAllowed() {
        return isStoreOrderAllowed;
    }

    public void setIsStoreOrderAllowed(boolean isStoreOrderAllowed) {
        this.isStoreOrderAllowed = isStoreOrderAllowed;
    }

    public String getOverrideText() {
        return overrideText;
    }

    public void setOverrideText(String overrideText) {
        this.overrideText = overrideText;
    }

    public boolean isIsSoldOut() {
        return isSoldOut;
    }

    public void setIsSoldOut(boolean isSoldOut) {
        this.isSoldOut = isSoldOut;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isIsPurchasable() {
        return isPurchasable;
    }

    public void setIsPurchasable(boolean isPurchasable) {
        this.isPurchasable = isPurchasable;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public boolean isIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(boolean isVirtual) {
        this.isVirtual = isVirtual;
    }

    public String getUpdateStartTime() {
        return updateStartTime;
    }

    public void setUpdateStartTime(String updateStartTime) {
        this.updateStartTime = updateStartTime;
    }
    
    
    
}
