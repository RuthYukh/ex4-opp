package ascii_art.img_to_char;

/**
 * creating an object of char and matching brightness
 * @author Ruth Yukhnovetsky
 */
public class CharAndBrightness {
    private char c;
    private double brightness;

    /**
     * constructor
     * @param c
     * @param brightness
     */
    public CharAndBrightness(char c, double brightness){
        this.c = c;
        this.brightness = brightness;
    }

    /**
     * brightness getter
     * @return brightness
     */
    public double getBrightness(){
        return brightness;
    }

    /**
     * brightness setter
     * @param newBrightness new brightness
     */
    public void setBrightness(double newBrightness){
        brightness = newBrightness;
    }

    /**
     * char getter
     * @return char
     */
    public char getChar(){
        return c;
    }
}
