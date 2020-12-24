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
class Financing {
    private String type;
    private String uiLabel;
    private String installments;

    public Financing(String type, String uiLabel, String installments) {
        this.type = type;
        this.uiLabel = uiLabel;
        this.installments = installments;
    }
    
    
    
}
