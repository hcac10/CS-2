package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EvilHangmanChooser implements IHangmanChooser {
    private static final Random RANDOM = new Random();

    //private int wordlength;
    private int maxGuesses;

    private String word = "";
    private SortedSet<Character> guesses = new TreeSet<Character>();
    private SortedSet<String> words = new TreeSet<>();


    public EvilHangmanChooser(int wordLength, int maxGuesses){
        if (wordLength < 1) {
            throw new IllegalArgumentException("Word Length is less than one");
        }
        if (maxGuesses < 1) {
            throw new IllegalArgumentException("Max Guesses is less than one");
        }
        Scanner s;
        try {
            s = new Scanner(new File("data/scrabble.txt"));
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.length() == wordLength) {
                    words.add(line);
                }
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        if (words.size() == 0) {
            throw new IllegalStateException("No words in dictionary with word length");
        }
        //this.wordlength = wordLength;
        this.maxGuesses = maxGuesses;
    }

  @Override
  public int makeGuess(char letter) {
      if (guesses.contains(letter)){
          throw new IllegalArgumentException("Character already guessed");
      }
      if(letter < 'a' || letter > 'z'){
          throw new IllegalArgumentException("Character should be lower case");
      }
      if (maxGuesses< 1){
          throw new IllegalStateException("Not enough guesses remaining");
      }
      Map<String, SortedSet<String>> maps = new TreeMap<>();
      guesses.add(letter);
      Character c = ' ';
      for(String s: words) {
          String guessWord = "";

          for (int i = 0; i < s.length(); i++) {
              if (guesses.contains(s.charAt(i))) {
                  guessWord += s.charAt(i);
              }else {
                  guessWord += "-";
              }
          }

          if (maps.containsKey(guessWord)) {
              maps.get(guessWord).add(s);
          }else {
              SortedSet<String> mapSet = new TreeSet<>();
              mapSet.add(s);
              maps.put(guessWord, mapSet);
            }

        }
        int largest = 0;
        //String pattern = "";
        for(SortedSet t: maps.values()){
            if(t.size() > largest){
                largest = t.size();
                words = t;
            }
        }
        //System.out.println(words);
        Character d = ' ';
        int count = 0;
        for(int i = 0; i < words.first().length(); i++) {
            d = words.first().charAt(i);
            if (d.equals(letter)) {
                count++;
            }
        }
      if(count == 0){
          maxGuesses--;
      }
    return count;
  }

  @Override
  public boolean isGameOver() {
      int c = 0;
      for(int i = 0; i < words.first().length(); i++){
          if(guesses.contains(words.first().charAt(i))){
              c++;
          }
      }
      if(c == words.first().length()){
          return true;
      }

      if(maxGuesses == 0){
          return true;
      }
      return false;
    }

  @Override
  public String getPattern() {
      String guessWord = "";
      for (int i = 0; i < words.first().length();  i++) {
          if (guesses.contains(words.first().charAt(i))){
              guessWord += words.first().charAt(i);
          } else {
              guessWord += "-";
          }
      }
      return guessWord;
  }

  @Override
  public SortedSet<Character> getGuesses() {
    return guesses;
  }

  @Override
  public int getGuessesRemaining() {
    return maxGuesses;
  }

  @Override
  public String getWord() {
    maxGuesses = 0;
    return words.first();
  }
}