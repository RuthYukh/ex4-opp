package ascii_art.img_to_char;

import image.Image;

import java.util.HashMap;

/**
 * converting an image to ascii representation by matching each brightness level to the right ascii letter
 *
 * @author Ruth Yukhnovetsky
 */
public class BrightnessImgCharMatcher {
    private Image image;
    private String fontName;
    private final HashMap<Image, Double> cache = new HashMap<>();

    private final int NUM_OF_PIXELS = 16;
    private final int NORMAL_DIVISION_VALUE = 255;

    /**
     * constructor
     *
     * @param image    given image to convert
     * @param fontName font of ascii letters
     */
    public BrightnessImgCharMatcher(Image image, String fontName) {
        this.image = image;
        this.fontName = fontName;
    }

    /**
     * Sorts the array with the chars from darkest to brightest,
     * preforms linear stretching according to request and returns the ascii image
     * that suits each brightness level.
     *
     * @param numCharsInRow number of characters in row
     * @param charSet       given array
     * @return ascii image
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        CharAndBrightness[] charAndBrightnesses = sortCharSetByBrightness(charSet);
        linearStretching(charAndBrightnesses);
        return convertImageToAscii(numCharsInRow, charAndBrightnesses);
    }

    private double getBrightness(Character character) {
        var charImg = CharRenderer.getImg(character, NUM_OF_PIXELS, fontName);

        int falseCounter = 0;
        for (boolean[] charRow : charImg) {
            for (boolean pixel : charRow) {
                if (pixel) {
                    falseCounter++;
                }
            }
        }
        return (double) falseCounter / NUM_OF_PIXELS;
    }

    private CharAndBrightness[] sortCharSetByBrightness(Character[] charSet) {
        //create new array
        CharAndBrightness[] charAndBrightness = new CharAndBrightness[charSet.length];
        for (int i = 0; i < charAndBrightness.length; i++) {
            charAndBrightness[i] = new CharAndBrightness(charSet[i], getBrightness(charSet[i]));
        }

        //sorting:
        //bubble sorting, not the most effective but amount of ascii chars is up to 96 so 96*96 is negligible
        int length = charAndBrightness.length;
        for (int i = 0; i < length - 1; i++)
            for (int j = 0; j < length - i - 1; j++)
                if (charAndBrightness[j].getBrightness() < charAndBrightness[j + 1].getBrightness()) {
                    // swap arr[j+1] and arr[j]
                    CharAndBrightness temp = charAndBrightness[j];
                    charAndBrightness[j] = charAndBrightness[j + 1];
                    charAndBrightness[j + 1] = temp;
                }
        return charAndBrightness;
    }


    private void linearStretching(CharAndBrightness[] charAndBrightnesses) {
        double minBrightness = charAndBrightnesses[charAndBrightnesses.length - 1].getBrightness();
        double maxBrightness = charAndBrightnesses[0].getBrightness();
        double maxToMin = maxBrightness - minBrightness;

        for (int i = 0; i < charAndBrightnesses.length; i++) {
            double newBrightness = (charAndBrightnesses[i].getBrightness() - minBrightness) / (maxToMin);
            charAndBrightnesses[i].setBrightness(newBrightness);
        }
    }

    private char[][] convertImageToAscii(int numCharsInRow, CharAndBrightness[] sortedArray) {
        int pixels = this.image.getWidth() / numCharsInRow;
        var height = this.image.getHeight() / pixels;

        char[][] result = new char[height][numCharsInRow];
        int counter = 0;
        int rowCounter = 0;

        //diving to sub images
        for (var subImage : this.image.squareSubImagesOfSize(pixels)) {
            double subImageBrightness = getSubImageBrightness(subImage);
            var matchingChar = assignAsciiCharToBrightness(sortedArray, subImageBrightness);
            result[rowCounter][counter] = matchingChar;
            counter += 1;
            if (counter == numCharsInRow) {
                counter = 0;
                rowCounter++;
            }
        }
        return result;
    }

    private double getSubImageBrightness(Image subImage) {
        if(cache.containsKey(subImage)){
            return cache.get(subImage);
        }
        double sum = 0;
        int counter = 0;

        for (var pixel : subImage.pixels()) {
            double greyPixel = pixel.getRed() * 0.2126 + pixel.getGreen() * 0.7152 +
                    pixel.getBlue() * 0.0722;
            sum += greyPixel;
            counter++;
        }
        var imageBrightness = sum / (counter * NORMAL_DIVISION_VALUE);
        cache.put(subImage, imageBrightness);
        return imageBrightness;
    }

    private Character assignAsciiCharToBrightness(CharAndBrightness[] charAndBrightnesses,
                                                  double valueToFind) {
        int low = charAndBrightnesses.length - 1;
        int high = 0;

        while (low - high > 1) {
            int middle = (low + high) / 2;

            if (charAndBrightnesses[middle].getBrightness() < valueToFind) {
                low = middle;
            } else if (charAndBrightnesses[middle].getBrightness() > valueToFind) {
                high = middle;
            } else {
                //charAndBrightnesses[middle].getBrightness() == valueToFind
                return charAndBrightnesses[middle].getChar();
            }
        }

        if (Math.abs(charAndBrightnesses[low].getBrightness() - valueToFind) <
                Math.abs(charAndBrightnesses[high].getBrightness() - valueToFind)) {
            return charAndBrightnesses[low].getChar();
        } else {
            return charAndBrightnesses[high].getChar();
        }
    }
}
