package de.eru.pherufxcontrols.utils;

/**
 *
 * @author Philipp Bruckner
 */
public enum ConfirmType {
    QUESTION("img/Question.png"),
    WARNING("img/Warning.png");
    
    private final String imagePath;
    
    private ConfirmType(final String imagePath){
        this.imagePath = imagePath;
    }
    
    public String getImagePath(){
        return imagePath;
    }
}
