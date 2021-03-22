package edu.caltech.cs2.project02.guessers;

import edu.caltech.cs2.project02.interfaces.IHangmanGuesser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class AIHangmanGuesser implements IHangmanGuesser {
  private static final String filer = "data/scrabble.txt";
  @Override
  public char getGuess(String pattern, Set<Character> guesses) {
    char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
    'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    char[] hold = new char[26 - guesses.size()];
    int c = 0;
    for (char a: alphabet)
      if(!guesses.contains(a)){
          hold[c] = a;
          c++;
      }
    SortedSet<String> setter = new TreeSet<>();
    try {
      Scanner s = new Scanner(new File(filer));
      while (s.hasNextLine()) {
        String line = s.nextLine();
        if (line.length() == pattern.length()) {
          setter.add(line);
        }
      }
    }
    catch(FileNotFoundException e) {
      e.printStackTrace();
    }
    SortedSet<String> set2 = new TreeSet<>();

    for (String b: setter){
      boolean check = true;
      for(int i = 0; i < pattern.length(); i++){
        if(pattern.charAt(i) == '-' && guesses.contains(b.charAt(i))){
          check = false;
          break;
        }
        else if(pattern.charAt(i) != '-'){
          if(pattern.charAt(i) != b.charAt(i)){
            check = false;
            break;
          }
        }
      }
      if(check){
        set2.add(b);
      }
    }
    Map<Character, Integer> maps = new HashMap<>();

    for(String b : set2){
      for (int i = 0; i < b.length(); i++){
        for(char h : hold){
          if(b.charAt(i) == h){
            if(maps.get(h) == null){
              maps.put(h,1);
            }else{
              maps.put(h, maps.get(h) + 1);
            }
          }
        }
      }
    }
    char best = ' ';
    int bestInt = 0;

    for(char b: maps.keySet()){
      if(bestInt < maps.get(b)){
        best = b;
        bestInt = maps.get(b);
      }
    }
    return best;

  }
}
