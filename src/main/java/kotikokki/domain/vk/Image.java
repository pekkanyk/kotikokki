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
class Image {
    
    @SerializedName("45")
    private String x45;
    @SerializedName("90")
    private String x90;
    @SerializedName("300")
    private String x300;
    @SerializedName("500")
    private String x500;
    @SerializedName("960")
    private String x960;
    private String orig;

    public Image(String orig) {
        this.orig = orig;
    }
    
    
}
