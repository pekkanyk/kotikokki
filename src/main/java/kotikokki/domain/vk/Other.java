/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain.vk;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author qru19
 */
class Other {
    @SerializedName("1")
    private double r1;
    @SerializedName("2")
    private double r2;
    @SerializedName("8")
    private double r8;
    @SerializedName("14")
    private double r14;

    public Other(double r1, double r2, double r8, double r14) {
        this.r1 = r1;
        this.r2 = r2;
        this.r8 = r8;
        this.r14 = r14;
    }
    
    
}
