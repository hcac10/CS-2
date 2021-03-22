package edu.caltech.cs2.lab02;

public class BombMain {
    public static void main(String[] args) {
        Bomb b = new Bomb();
        b.phase0("22961293");
        b.phase1("hdc");
        String test = "";
        for(int i = 0; i < 5000; ++i)
            test += "1 ";
        test += "1374866960 ";
        for(int i = 0; i < 5000; ++i)
            test += "1 ";
        b.phase2(test);
    }
}