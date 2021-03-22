package edu.caltech.cs2.project04;

import edu.caltech.cs2.datastructures.ArrayDeque;
import edu.caltech.cs2.datastructures.TrieMap;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ITrieMap;


public class TrieMovieAutoCompleter extends AbstractMovieAutoCompleter {
    private static ITrieMap<String, IDeque<String>, IDeque<String>> titles = new TrieMap<>((IDeque<String> s) -> s);

    public static void populateTitles() {
        for(String c: ID_MAP.keySet()){
//            String[] helper = c.split(" ");
//            for(String t: helper)
//                temporary.add(t);
////            titles.put(temporary, ans);
//            for(int j = 0; j < c.length(); ++j){
//                IDeque<String> temporary = new ArrayDeque<>();
//                if(c.charAt(j) == ' ') {
//                    temporary.add(c.substring(j+1).toLowerCase());
//                    IDeque<String> ans = new ArrayDeque<>();
//                    ans.add(c);
//                }
//            }
            String[] helper = c.split(" ");
            for(int i = 0; i < helper.length; ++i){
                IDeque<String> temporary = new ArrayDeque<>();
                for(int j = i; j < helper.length; ++j)
                    temporary.addBack(helper[j].toLowerCase());
                IDeque<String> ans = new ArrayDeque<>();
                ans.addBack(c);
                if(!titles.containsKey(temporary))
                    titles.put(temporary, ans);
                else{
                    IDeque<String> temp = titles.get(temporary);
                    temp.add(c);
                    titles.put(temporary, temp);
                }
            }
//
//            IDeque<String> temporary = new ArrayDeque<>();
//            temporary.add(c.toLowerCase());

        }
    }

    public static IDeque<String> complete(String term) {
        IDeque<String> ans = new ArrayDeque<>();
        IDeque<String> key = new ArrayDeque<>();
        String[] helper = term.split(" ");
        for(String c: helper)
            key.addBack(c);
        for (IDeque<String> comp : titles.getCompletions(key)) {
            for (String s : comp) {
                if(!ans.contains(s))
                    ans.addBack(s);
            }
        }
        return ans;
    }
}
