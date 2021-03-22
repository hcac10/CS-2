package edu.caltech.cs2.lab06;

import java.util.ArrayList;
import java.util.List;

public class AnagramGenerator {
    public static void printPhrases(String phrase, List<String> dictionary) {
        List<String> acc = new ArrayList<String>();
        helpPhrases(new LetterBag(phrase), dictionary, acc);
    }

    private static void helpPhrases(LetterBag remaining, List<String> dictionary, List<String> acc) {
        if (remaining.isEmpty()) {
            System.out.println(acc);
        } else {
            for (String s : dictionary) {
                LetterBag temp = new LetterBag(s);
                LetterBag diff = remaining.subtract(temp);
                if (diff == null)
                    continue;
                else {
                    acc.add(s);
                    helpPhrases(diff, dictionary, acc);
                    acc.remove(s);
                }
            }
        }

    }

    public static void printWords(String word, List<String> dictionary) {
        LetterBag temp = new LetterBag(word);
        for (String s : dictionary) {
            if (word.length() != s.length())
                continue;
            else {
                LetterBag other = new LetterBag(s);
                if(temp.subtract(other) != null)
                    if(temp.subtract(other).isEmpty())
                        System.out.println(s);
            }
        }
    }
}
