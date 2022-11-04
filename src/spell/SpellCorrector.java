package spell;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class SpellCorrector implements ISpellCorrector {
    Trie trie = new Trie();
    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        String word;
        File inputFile = new File(dictionaryFileName);
        Scanner scan = new Scanner(inputFile);
        while(scan.hasNext()) {
            word = scan.next().toLowerCase();
            trie.add(word);
        }
        scan.close();
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        ArrayList<String> posWord = possibleWords(inputWord);
        ArrayList<String> posWord2 = new ArrayList<>();
        ArrayList<String> pairs = new ArrayList<>();
        inputWord = inputWord.toLowerCase();

        if((trie.find(inputWord) != null) && (trie.find(inputWord).getValue() > 0)) {
            return inputWord;
        }

        for(int i = 0; i < posWord.size(); i++) {
            if((trie.find(posWord.get(i)) != null) && (trie.find(posWord.get(i)).getValue() > 0)) {
                pairs.add(posWord.get(i));
            }
        }

        if(pairs.size() == 0) {
            for(int i = 0; i < posWord.size(); i++) {
                ArrayList<String> temp = possibleWords(posWord.get(i));
                for(int j = 0; j < temp.size(); j++) {
                    posWord2.add(temp.get(j));
                }
            }
            for(int i = 0; i < posWord2.size(); i++) {
                if(trie.find(posWord2.get(i)) != null && trie.find(posWord2.get(i)).getValue() > 0) {
                    pairs.add(posWord2.get(i));
                }
            }
            if(pairs.size() == 0) {
                return null;
            }
        }
        //System.out.println(pairs);
        if (pairs.size() > 1) {
            pairs = getHighCount(pairs);
            //System.out.println(pairs);
            if(pairs.size() > 1) {
                //System.out.println(pairs);
                //System.out.println("In if loop");
                return firstAlphaWord(pairs);
            }
        }
        //System.out.println(pairs);
        return pairs.get(0);
    }

    private ArrayList<String> possibleWords(String inputWord) {
        ArrayList<String> posWord = new ArrayList<>();
        int wordSize = inputWord.length();
        char insertLetter;
        //deletion
        for(int i = 0; i < wordSize; i++) {
            posWord.add(inputWord.substring(0, i) + inputWord.substring(i + 1, wordSize));
            //alternation
            for(int j = 0; j < 26; j++) {
                insertLetter = (char)(j + 'a');
                posWord.add(inputWord.substring(0, i) + insertLetter + inputWord.substring(i + 1, wordSize));
            }
        }
        //insertion
        for(int i = 0; i <= wordSize; i++) {
            for(int j = 0; j < 26; j++) {
                insertLetter = (char)(j + 'a');
                posWord.add(inputWord.substring(0, i) + insertLetter + inputWord.substring(i, wordSize));
            }
        }
        //transposition
        for(int i = 0; i < wordSize - 1; i++) {
            posWord.add(inputWord.substring(0, i) + inputWord.substring(i+1, i+2) + inputWord.substring(i, i+1) +
                    inputWord.substring(i+2, wordSize));
            //System.out.println(posWord);
        }
        System.out.println(posWord);
        return posWord;
    }
    //function is meant to monitor then number of times a word appears and compare it to another word. Chooses the one with thehighest count
    private ArrayList<String> getHighCount(ArrayList<String> matches) {
        int maxCount = 0;
        int currentCount;
        for (int i = 0; i < matches.size(); i++) {
            currentCount = trie.find(matches.get(i)).getValue();
            //maxCount = currentCount > maxCount ? currentCount : maxCount;
            if (currentCount > maxCount) {
                maxCount = currentCount;
            }
            /*else if (currentCount == maxCount) { // Do i need to check if the counts are equal?
                //system.out.println("The counts are equal");
            }*/
        }
        for (int i = matches.size() -1; i >= 0; i--) {
            if (trie.find(matches.get(i)).getValue() < maxCount) {
                matches.remove(i);
            }
        }
        //System.out.println(matches);
        return matches;
    }

    private String firstAlphaWord(ArrayList<String> matches) {
        String firstAlphaWord = "zzz";
        //System.out.println(matches);
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).compareTo(firstAlphaWord) < 0) {
                //system.out.println("Entered if loop");
                firstAlphaWord = matches.get(i);
            }
            //System.out.println(firstAlphaWord);
        }
        return firstAlphaWord;
    }
}
