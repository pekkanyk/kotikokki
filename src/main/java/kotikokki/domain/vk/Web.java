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
public class Web {
    private transient String stockStatus;
    private transient int minDays;
    private transient int maxDays;
    private transient boolean isAvailable;
    private transient boolean isPurchasable;
    private transient int stock;
    private transient String ranking;
    private Warehouses warehouses;

    
    public Warehouses getWarehouses() {
        return warehouses;
    }
    
    
    
}
