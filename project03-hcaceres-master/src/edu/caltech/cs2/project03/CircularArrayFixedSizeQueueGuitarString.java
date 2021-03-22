package edu.caltech.cs2.project03;

import edu.caltech.cs2.datastructures.CircularArrayFixedSizeQueue;
import edu.caltech.cs2.interfaces.IFixedSizeQueue;
import edu.caltech.cs2.interfaces.IQueue;

import java.util.Random;


public class CircularArrayFixedSizeQueueGuitarString {
    private static final double inFrec = 44100;
    private static final double gFact = 0.996;
    private static Random rand = new Random();
    private IFixedSizeQueue<Double> guitar;

    public CircularArrayFixedSizeQueueGuitarString(double frequency) {
        int n = (int) (Math.ceil(inFrec / frequency));
        this.guitar = new CircularArrayFixedSizeQueue<>(n);
        for (int i = 0; i < n; i++){
            this.guitar.enqueue(0.0);
        }
    }

    public int length() {
        return guitar.size();
    }

    public void pluck() {
        for(int i = 0; i < length(); i++){
            this.guitar.dequeue();
            this.guitar.enqueue(rand.nextDouble()- 0.5);
        }
    }

    public void tic() {
        double note = this.guitar.dequeue();
        double note2 = this.guitar.peek();
        this.guitar.enqueue(gFact * 1/2 * (note + note2));
    }

    public double sample() {
        return this.guitar.peek();
    }
}
