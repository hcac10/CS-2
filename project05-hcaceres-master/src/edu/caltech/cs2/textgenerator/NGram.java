package edu.caltech.cs2.textgenerator;

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
        IDeque<String> helper = ((NGram) other).data;
        Iterator<String> help = helper.iterator();
        Iterator<String> original = this.data.iterator();
        while(help.hasNext() && original.hasNext()){
            int answer = original.next().compareTo(help.next());
            if (answer != 0){
                return answer;
            }
            if (help.hasNext() && !original.hasNext()){
                return -1;
            }
            else if (original.hasNext() && !help.hasNext()){
                return 1;
            }
        }
        return 0;
    }
    //if a hashcode is greater than b return 1, if equal return, else return -1
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NGram)) {
            return false;
        }

        else {
            NGram temp = (NGram) o;
            if (temp.data.size()!= this.data.size()){
                return false;
            }
            else{
                Iterator<String> tempit = temp.data.iterator();
                Iterator<String> NGramit = data.iterator();
                while(tempit.hasNext() && NGramit.hasNext()){
                    String string1 =tempit.next();
                    String string2 =  NGramit.next();
                    if (!string1.equals(string2)){
                        return false;
                    }
                }
            }
        }
        return true;
    }


    @Override
    public int hashCode() {
        //hashcode said in lecture but 37 instead of 31. multiply each string by bigger power of 37 count *= 37
        int answer = 0;
        int factor = 1;
        int prime = 37;
        for (String i : this.data) {
            answer += factor * i.hashCode();
            factor *= prime;
        }
        if (answer < 0){
            answer *= -1;
        }
        return answer;
    }
}