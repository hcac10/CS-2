package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.HangmanGame;
import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RandomHangmanChooser implements IHangmanChooser {

  private static final Random RANDOM = new Random();

  private int wordlength;
  private int maxGuesses;

  private final String word;
  private SortedSet<Character> guesses = new TreeSet<Character>();

  public RandomHangmanChooser(int wordLength, int maxGuesses){
      if (wordLength < 1) {
          throw new IllegalArgumentException("Word Length is less than one");
      }
      if (maxGuesses < 1) {
          throw new IllegalArgumentException("Max Guesses is less than one");
      }

      Scanner s;
      SortedSet<String> words = new TreeSet<>();
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

      String chosen = "";
      if (words.size() == 0) {
          throw new IllegalStateException("No words in dictionary with word length");
      }

            int index = RANDOM.nextInt(words.size());
            List<String> wordsList = new ArrayList<>(words);
            chosen = wordsList.get(index);


            this.wordlength = wordLength;
            this.maxGuesses = maxGuesses;
            this.word = chosen;

        }



  //@Override
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

    List<Integer> loc = new ArrayList<Integer>();
    String s = "";
    Character c = ' ';
    int count = 0;
    boolean wrong = false;
    for (int i = 0; i < word.length(); i++){
      c = word.charAt(i);
      if(c.equals(letter)){
          count++;
          loc.add(i);
          wrong = true;
      }
    }

    if(wrong == false){
        maxGuesses--;
    }

    guesses.add(letter);
    return count;
  }

  @Override
  public boolean isGameOver() {
    if(getPattern().equals(this.word)){
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
      for (int i = 0; i < wordlength; i++){
        if(guesses.contains(word.charAt(i))){
            guessWord += word.charAt(i);
        }
        else{
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
      return word;
  }
}