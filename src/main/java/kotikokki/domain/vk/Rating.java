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
class Rating {
    private Other other;
    private int recommendedCount;
    private int reviewCount;
    private int averageOverallRating;

    public Rating(Other other, int recommendedCount, int reviewCount, int averageOverallRating) {
        this.other = other;
        this.recommendedCount = recommendedCount;
        this.reviewCount = reviewCount;
        this.averageOverallRating = averageOverallRating;
    }
    
    
}
