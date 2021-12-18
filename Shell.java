package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * running the program
 * @author Ruth Yukhnovetsky
 */
public class Shell {
    private Image img;
    private HashSet<Character> characters;
    private final int FIRST_ASCII_CHAR = 33;
    private final int LAST_ASCII_CHAR = 127;

    private static final String FONT_NAME = "Courier New";
    private static final String OUTPUT_FILENAME = "out.html";

    private AsciiOutput outputType;

    private static final int MIN_PIXELS_PER_CHAR = 2;
    private static final int INITIAL_CHARS_IN_ROW = 64;
    private final int minCharsInRow;
    private final int maxCharsInRow;
    private int charsInRow;
    private BrightnessImgCharMatcher brightnessImgCharMatcher;

    /**
     * constructor
     * @param img ing to convert
     */
    public Shell(Image img) {
        outputType = new HtmlAsciiOutput(OUTPUT_FILENAME, FONT_NAME);

        brightnessImgCharMatcher = new BrightnessImgCharMatcher(img, FONT_NAME);
        minCharsInRow = Math.max(1, img.getWidth() / img.getHeight());
        maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow), minCharsInRow);

        this.img = img;
        characters = new HashSet<Character>();
        characters.addAll(List.of(new Character[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}));
    }

    /**
     * interpreting user's input
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        while (!input.equals("exit")) {
            System.out.print(">>>>>");
            input = scanner.nextLine();
            if (input.equals("chars")) {
                printAllChars();
                continue;
            }
            if (input.startsWith("add")) {
                handleAddOperation(input);
                continue;
            }
            if (input.startsWith("remove")) {
                handleRemoveOperation(input);
                continue;
            }
            if (input.equals("res up")) {
                handleResUp();
                continue;
            }
            if (input.equals("res down")) {
                handleResDown();
                continue;
            }
            if (input.equals("console")) {
                outputType = new ConsoleAsciiOutput();
                continue;
            }
            if (input.equals("html")) {
                outputType = new HtmlAsciiOutput(OUTPUT_FILENAME, FONT_NAME);
                continue;
            }
            if (input.equals("render")) {
                outputType.output(brightnessImgCharMatcher.chooseChars(charsInRow,
                        getCharsArray()));
                continue;
            }
            System.out.println("illegal input");
        }
    }

    private Character[] getCharsArray() {
       var arrayOfObject =  characters.toArray();
        Character[] result = new Character[arrayOfObject.length];
        for (int i= 0; i< result.length; i ++){
            result[i] = (char) arrayOfObject[i];
        }
        return result;
    }

    private void handleResDown() {
        if (charsInRow / 2 >= minCharsInRow && charsInRow / 2 <= maxCharsInRow) {
            charsInRow /= 2;
            System.out.println("Width set to " + charsInRow);
        } else {
            System.out.println("illegal sizing down");
        }
    }

    private void handleResUp() {
        if (charsInRow * 2 <= maxCharsInRow && charsInRow * 2 >= minCharsInRow) {
            charsInRow *= 2;
            System.out.println("Width set to " + charsInRow);
        } else {
            System.out.println("illegal sizing up");
        }
    }

    private void handleRemoveOperation(String input) {
        String[] splittedInput = input.split(" ");
        if (splittedInput.length != 2) {
            System.out.println("Invalid number of parameters to add ");
        }
        if (splittedInput[1].length() == 1) {
            characters.remove(splittedInput[1].charAt(0));
        }
        if (splittedInput[1].equals("all")) {
            characters.removeAll(asciiCharsInRange(FIRST_ASCII_CHAR, LAST_ASCII_CHAR));
        }
        if (splittedInput[1].equals("space")) {
            characters.remove(' ');
        }
        String[] splittedCharsRange = splittedInput[1].split("-");
        if (splittedCharsRange.length == 2) {
            int low = splittedCharsRange[0].charAt(0);
            int high = splittedCharsRange[1].charAt(0);
            characters.removeAll(asciiCharsInRange(low, high));
        }
    }

    private void handleAddOperation(String input) {
        String[] splittedInput = input.split(" ");
        if (splittedInput.length != 2) {
            System.out.println("Invalid number of parameters to add ");
        }
        if (splittedInput[1].length() == 1) {
            characters.add(splittedInput[1].charAt(0));
        }
        if (splittedInput[1].equals("all")) {
            characters.addAll(asciiCharsInRange(FIRST_ASCII_CHAR, LAST_ASCII_CHAR));
        }
        if (splittedInput[1].equals("space")) {
            characters.add(' ');
        }
        String[] splittedCharsRange = splittedInput[1].split("-");
        if (splittedCharsRange.length == 2) {
            int low = splittedCharsRange[0].charAt(0);
            int high = splittedCharsRange[1].charAt(0);

            characters.addAll(asciiCharsInRange(low, high));
        }
    }

    private ArrayList<Character> asciiCharsInRange(int low, int high) {
        if (low > high) {
            int temp = low;
            low = high;
            high = temp;
        }

        ArrayList<Character> allChars = new ArrayList<>();
        for (int i = low; i < high + 1; i++) {
            allChars.add((char) i);
        }
        return allChars;
    }

    private void printAllChars() {
        System.out.println("All Chars:");
        for (Character character : characters) {
            System.out.print(character + " ");
        }
    }
}
