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
public class Warehouses {
    private Warehouse js;
    private transient Warehouse pak;

 

    public Warehouse getJs() {
        return js;
    }
    
    
    
}
