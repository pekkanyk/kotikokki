/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain.vk;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author qru19
 */
@Data
@AllArgsConstructor
public class Discount {
    private int id;
    private String endAt;
    private String beginAt;
    private int discountType;
    private String name;

    public int getId() {
        return id;
    }

    public String getEndAt() {
        return endAt;
    }

    public String getBeginAt() {
        return beginAt;
    }

    public int getDiscountType() {
        return discountType;
    }

    public String getName() {
        return name;
    }
    
    public LocalDate kampanjanLoppuPaiva(){
        LocalDate endDay = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (endAt != null) {
            endDay = LocalDate.parse(this.endAt.substring(0, Math.min(this.endAt.length(), 10)), formatter);
        }
        
        return endDay;
    }
    
    
    
}
