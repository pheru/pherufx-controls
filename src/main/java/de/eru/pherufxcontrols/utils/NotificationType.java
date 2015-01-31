package de.eru.pherufxcontrols.utils;

/**
 *
 * @author Philipp Bruckner
 */
public enum NotificationType {
    INFO("img/Info.png"), 
    WARNING("img/Warning.png"), 
    ERROR("img/Error.png");
    
    private final String imagePath;
    
    private NotificationType(final String imagePath){
        this.imagePath = imagePath;
    }
    
    public String getImagePath(){
        return imagePath;
    }
}
