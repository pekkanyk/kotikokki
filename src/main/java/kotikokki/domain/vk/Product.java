/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotikokki.domain.vk;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author qru19
 */
@Data
@AllArgsConstructor
public class Product{
    private transient int updateCount;
    private transient int maxAmountPerOrder;
    private boolean active;
    private transient Description description;
    private transient String[] eans;
    private transient DescriptionShort descriptionShort;
    private String createdAt;
    private Price price;
    private transient Restrictions restrictions;
    private transient Name name;
    private transient Brand brand;
    private transient String updatedAt;
    private int visible;
    private transient String freeShipmentFor;
    private boolean isFireSale;
    private transient Image[] images;
    private String warranty;
    private transient Extended[] extended;
    private boolean vak;
    private String productId;
    private int pid;
    private transient boolean electronic;
    private transient ShipmentPrices[] shipmentPrices;
    private transient ShopArea[] shopAreas;
    private transient Image[] marketingImages;
    private transient Category category;
    private transient String[] ribbons;
    private transient int popularity;
    private transient String verifiedAt;
    private transient Product[] bundleProducts;
    private transient Bulletpoints bulletPoints;
    private transient Financing financing;
    private transient boolean installationsAvailable;
    private transient Relations relations;
    private transient Links[] links;
    private transient int vakType;
    private transient String[] mpns;
    @SerializedName("package")
    private  PackageName packageName;
    private transient boolean hasDetails;
    private transient int ageLimit;
    private transient boolean hidePriceEstimate;
    private transient String[] categories;
    private transient Rating rating;
    private transient String releasedAt;
    private transient Href href;
    private transient String updateStartTime;
    private transient String[] demoLocations;
    private transient String[] campaigns;
    private Availability availability;
    private CustomerReturnsInfo customerReturnsInfo;

    public boolean isActive() {
        return active;
    }

    public Availability getAvailability() {
        return availability;
    }
    

    public Price getPrice() {
        return price;
    }

    public Name getName() {
        return name;
    }

    public int getVisible() {
        return visible;
    }

    public boolean isIsFireSale() {
        return isFireSale;
    }

    public String getWarranty() {
        return warranty;
    }

    public int getPid() {
        return pid;
    }

    public CustomerReturnsInfo getCustomerReturnsInfo() {
        return customerReturnsInfo;
    }

    
}

