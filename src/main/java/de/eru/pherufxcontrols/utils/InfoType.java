package de.eru.pherufxcontrols.utils;

/**
 *
 * @author Philipp Bruckner
 */
public enum InfoType {
    INFO("img/Info.png"), 
    WARNING("img/Warning.png"), 
    ERROR("img/Error.png");
    
    private final String imagePath;
    
    private InfoType(final String imagePath){
        this.imagePath = imagePath;
    }
    
    public String getImagePath(){
        return imagePath;
    }
}
