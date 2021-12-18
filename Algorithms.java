package ascii_art;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *algo portion of the exercise
 * @author Ruth Yukhnovetsky
 */
public class Algorithms {

    /**
     * #1 the famous leetcode 287 problem
     * @return duplicated num in array
     */
    public static int findDuplicate(int[] numList) {
        int firstIndex = numList[0];
        int lastIndex = numList[0];

        while (true){
            firstIndex = numList[firstIndex];
            lastIndex = numList[numList[lastIndex]];
            if (firstIndex == lastIndex){
                break;
            }
        }
        int temp1 = numList[0];
        int temp2 = firstIndex;
        while (temp1 != temp2){
            temp1 = numList[temp1];
            temp2 = numList[temp2];
        }
        return temp1;
    }

    /**
     * find num of unique morse representations for words in given list
     * @param words array of given lists
     * @return number of unique morse representations
     */
    public static int uniqueMorseRepresentations(String[] words) {
        HashMap<Character, String> dictionary = new HashMap<>();
        String[] morseLetters = new String[] {".-","-...","-.-.","-..",".","..-.","--.","....","..",
                ".---","-.- ",".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...- ",
                ".--","-..-","-.--","--.."};

        int indexOdMorseLettersArray = 0;
        for (char ch = 'a'; ch <= 'z'; ++ch) {
            dictionary.put(ch, morseLetters[indexOdMorseLettersArray]);
            indexOdMorseLettersArray++;
        }

        HashSet<String> possibleMorseStrings = new HashSet<>();

        for (var word : words){
            String morseString = "";
            for (int i = 0 ; i < word.length() ; i++){
                morseString += dictionary.get(word.charAt(i));
            }
            possibleMorseStrings.add(morseString);
        }

        return possibleMorseStrings.size();
    }

}
