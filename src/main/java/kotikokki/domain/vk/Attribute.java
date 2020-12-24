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
class Attribute {
    private String type;
    private AttributeLocal name;
    private String value;
    private AttributeLocal valueLocal;
    private AttributeCategory category;
    private String fieldType;

    public Attribute(String type, AttributeLocal name, String value, AttributeCategory category, String fieldType) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.category = category;
        this.fieldType = fieldType;
    }
    
    public Attribute(String type, AttributeLocal name, AttributeLocal value, AttributeCategory category, String fieldType) {
        this.type = type;
        this.name = name;
        this.valueLocal = value;
        this.category = category;
        this.fieldType = fieldType;
    }
    
    
}
