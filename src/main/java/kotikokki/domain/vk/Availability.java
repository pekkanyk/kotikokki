/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain.vk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author qru19
 */
@Data
@AllArgsConstructor
public class Availability {
    private transient String _id;
    private transient boolean isPickupAllowed;
    private transient boolean hasStock;
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
    private transient boolean isPurchasable;
    private transient int pid;
    private transient String[] tags;
    private transient boolean isVirtual;
    private transient String updateStartTime;


    public boolean isIsEOL() {
        return isEOL;
    }

    public Stocks getStocks() {
        return stocks;
    }
    
    
    
}
