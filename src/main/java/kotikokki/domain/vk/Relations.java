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
class Relations {
    private int[] bundles;
    private int[] newer;
    private int[] recommended;
    private int[] older;

    public Relations(int[] bundles, int[] newer, int[] recommended, int[] older) {
        this.bundles = bundles;
        this.newer = newer;
        this.recommended = recommended;
        this.older = older;
    }



    public Relations() {
    }
    
    
}
