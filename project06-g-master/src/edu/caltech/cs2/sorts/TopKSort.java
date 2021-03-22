package edu.caltech.cs2.sorts;

import edu.caltech.cs2.datastructures.MinFourHeap;
import edu.caltech.cs2.interfaces.IPriorityQueue;

public class TopKSort {
    /**
     * Sorts the largest K elements in the array in descending order.
     * @param array - the array to be sorted; will be manipulated.
     * @param K - the number of values to sort
     * @param <E> - the type of values in the array
     */
    public static <E> void sort(IPriorityQueue.PQElement<E>[] array, int K) {
        if (K < 0) {
            throw new IllegalArgumentException("K cannot be negative!");
        }
        if(array.length < K)
            return;
        MinFourHeap temp = new MinFourHeap();
        IPriorityQueue.PQElement<E>[] ans = new IPriorityQueue.PQElement[array.length];
        for(int i = 0; i < array.length; ++i){
            temp.enqueue(array[i]);
        }
        for(int i = 0; i < array.length; ++i){
            ans[array.length - 1 - i] = temp.dequeue();
        }
        for(int i = 0; i < K; ++i)
            array[i] = ans[i];
        for(int i = K; i < array.length; ++i)
            array[i] = null;
    }
}
