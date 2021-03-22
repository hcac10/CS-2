package edu.caltech.cs2.types;

import edu.caltech.cs2.datastructures.LinkedDeque;
import edu.caltech.cs2.interfaces.IDeque;

import java.util.Iterator;

public class NGram implements Iterable<String>, Comparable<NGram> {
    public static final String NO_SPACE_BEFORE = ",?!.-,:'";
    public static final String NO_SPACE_AFTER = "-'><=";
    public static final String REGEX_TO_FILTER = "”|\"|“|\\(|\\)|\\*";
    public static final String DELIMITER = "\\s+|\\s*\\b\\s*";
    private IDeque<String> data;

    public static String normalize(String s) {
       return s.replaceAll(REGEX_TO_FILTER, "").strip();
    }

    public NGram(IDeque<String> x) {
        this.data = new LinkedDeque<>();
        for (int i = 0; i < x.size(); i++) {
            this.data.addBack(x.peekFront());
            x.addBack(x.removeFront());
        }
    }

    public NGram(String data) {
        this(normalize(data).split(DELIMITER));
    }

    public NGram(String[] data) {
        this.data = new LinkedDeque<>();
        for (String s : data) {
            s = normalize(s);
            if (!s.isEmpty()) {
                this.data.addBack(s);
            }
        }
    }

    public NGram next(String word) {
        String[] data = new String[this.data.size()];
        for (int i = 0; i < data.length - 1; i++) {
            this.data.addBack(this.data.removeFront());
            data[i] = this.data.peekFront();
        }
        this.data.addBack(this.data.removeFront());
        data[data.length - 1] = word;
        return new NGram(data);
     }

    public String toString() {
        String result = "";
        String prev = "";
        for (String s : this.data) {
            result += ((NO_SPACE_AFTER.contains(prev) || NO_SPACE_BEFORE.contains(s) || result.isEmpty()) ?  "" : " ") + s;
            prev = s;
        }
        return result.strip();
    }

    @Override
    public Iterator<String> iterator() {
        return this.data.iterator();
    }


    @Override
    public int compareTo(NGram other) {
        int hashthis = this.hashCode();
        int hashelse = other.hashCode();
        if(hashthis > hashelse)
            return 1;
        else if(hashthis < hashelse)
            return -1;
        else
            return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NGram)) {
            return false;
        }
        Iterator<String> thisit = this.iterator();
        NGram temp = (NGram) o;
        Iterator<String> other = temp.iterator();
        while(thisit.hasNext()) {
            if (other.hasNext()) {
                String a = thisit.next();
                String b = other.next();
                if (!a.equals(b))
                    return false;
            } else
                return false;
        }
        if(other.hasNext())
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int ans = 0;
        int val = 1;
        for(String s: data) {
//            for (int i = 0; i < str.length(); ++i) {
            ans += val * s.hashCode();
            val *= 37;
//            }
        }
        return ans;
    }

    private String reverse(String inp){
        String ans = "";
        if(inp.equals(""))
            return "";
        for(int i = 0; i < inp.length(); ++i){
            ans += inp.charAt(inp.length() - i - 1);
        }
        return ans;
    }
}