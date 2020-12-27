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
public class Price {
    private double original;
    private transient double currentTaxless;
    private transient int discountPercentage;
    private transient double originalTaxless;
    private transient String unit;
    private Discount discount;
    private transient String originalFormatted;
    private transient double deposit;
    private transient String currentFormatted;
    private transient double taxRate;
    private double discountAmount;
    private double current;

    public double getCurrent() {
        return current;
    }

    public double getOriginal() {
        return original;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public Discount getDiscount() {
        return discount;
    }
    
    
    
    
    
    
}
