package edu.caltech.cs2.project04;

import edu.caltech.cs2.datastructures.ArrayDeque;
import edu.caltech.cs2.interfaces.IDeque;

import java.util.HashMap;
import java.util.Map;

public class HashMovieAutoCompleter extends AbstractMovieAutoCompleter {
    private static Map<String, IDeque<String>> titles = new HashMap<>();

    public static void populateTitles() {
//        titles.clear();
        Map<String, String> temp = AbstractMovieAutoCompleter.getIDMap();
        for(String c: temp.keySet()){
            IDeque<String> temporary = new ArrayDeque<>();
            titles.put(c, temporary);
        }
        for(String c: titles.keySet()){
            titles.get(c).add(c);
            for(int j = 0; j < c.length(); ++j){
                if(c.charAt(j) == ' ')
                    if(!titles.get(c).contains(c.substring(j+1)))
                        titles.get(c).add(c.substring(j+1));
            }
        }
    }

    public static IDeque<String> complete(String term) {
        IDeque<String> ans = new ArrayDeque<>();
        //each movie
        for(String movie: titles.keySet()){
            IDeque<String> answer = titles.get(movie);
            //each suffix
            for(String suff: answer){
                if(suff.length() > term.length()) {
                    String lower = suff.substring(0,term.length()).toLowerCase();
                    String other = term.toLowerCase();
                    if(lower.equals(other)){
                        if(suff.charAt(lower.length()) != ' ') {
                            continue;
                        }
                        else{
                            if(!ans.contains(movie)) {
                                ans.addFront(movie);
                            }
                        }
                    }
                }
                else if(suff.length() == term.length())
                    if(suff.toLowerCase().equals(term.toLowerCase()))
                        if(!ans.contains(movie)) {
                            ans.addFront(movie);
                        }
            }
        }
        return ans;
    }
}