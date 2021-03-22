package edu.caltech.cs2.project01;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Crytogram {
    public static final String FILE = "cryptogram.txt";

    public static void main(String[] args) throws FileNotFoundException{
        Scanner s = new Scanner(new File(FILE));
        String newest = "";
        while (s.hasNextLine()){
            String line = s.nextLine();
            for (int i = 0; i < line.length(); i++){
                    if(Character.isUpperCase(line.charAt(i))){
                        newest += line.charAt(i);
                }
            }
        }
        QuadGramLikelihoods likelihoods = new QuadGramLikelihoods();
        SubstitutionCipher best = new SubstitutionCipher(newest);
        for (int i = 0; i < 20; i ++) {
            SubstitutionCipher cipher = best.getSolution(likelihoods);
            if (cipher.getScore(likelihoods) > best.getScore(likelihoods)) {
                best = cipher;
            }
        }
        System.out.println(best.getPlainText());
    }
}

