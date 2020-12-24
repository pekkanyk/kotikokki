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
public class CustomerReturnsInfo {
    private int tax;
    private int warranty;
    private int id;
    private String condition;
    private String product_name;
    private int state;
    private double price_without_tax;
    private String product_extra_info;
    private double price_with_tax;
    private int pid;
    private String nanufacturers_code;

    public CustomerReturnsInfo(int tax, int warranty, int id, String condition, String product_name, int state, double price_without_tax, String product_extra_info, double price_with_tax, int pid, String nanufacturers_code) {
        this.tax = tax;
        this.warranty = warranty;
        this.id = id;
        this.condition = condition;
        this.product_name = product_name;
        this.state = state;
        this.price_without_tax = price_without_tax;
        this.product_extra_info = product_extra_info;
        this.price_with_tax = price_with_tax;
        this.pid = pid;
        this.nanufacturers_code = nanufacturers_code;
    }

    public int getTax() {
        return tax;
    }

    public int getWarranty() {
        return warranty;
    }

    public int getId() {
        return id;
    }

    public String getCondition() {
        return condition;
    }

    public String getProduct_name() {
        return product_name;
    }

    public int getState() {
        return state;
    }

    public double getPrice_without_tax() {
        return price_without_tax;
    }

    public String getProduct_extra_info() {
        return product_extra_info;
    }

    public double getPrice_with_tax() {
        return price_with_tax;
    }

    public int getPid() {
        return pid;
    }

    public String getNanufacturers_code() {
        return nanufacturers_code;
    }
    
    
    
}
