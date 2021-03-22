package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IPriorityQueue;

import java.util.Iterator;

public class MinFourHeap<E> implements IPriorityQueue<E> {

    private static final int DEFAULT_CAPACITY = 5;

    private int size;
    private PQElement<E>[] data;
    private IDictionary<E, Integer> keyToIndexMap;

    /**
     * Creates a new empty heap with DEFAULT_CAPACITY.
     */
    public MinFourHeap() {
        this.size = 0;
        this.data = new PQElement[DEFAULT_CAPACITY];
        this.keyToIndexMap = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }

    @Override
    public void increaseKey(PQElement<E> key) {
        int idx = -1;
        for(int i = 0; i < this.size; ++i){
            if(this.data[i].data.equals(key.data)){
                idx = i;
                break;
            }
        }
        if(idx == -1)
            throw new IllegalArgumentException("Key is not in heap.");
        this.data[idx] = key;
        percolateDown(idx);
    }

    @Override
    public void decreaseKey(PQElement<E> key) {
        int idx = -1;
        for(int i = 0; i < this.size; ++i){
            if(this.data[i].data.equals(key.data)){
                idx = i;
            }
        }
        if(idx == -1)
            throw new IllegalArgumentException("Key is not in heap.");
        this.data[idx] = key;
        percolateUp(idx);
    }

    @Override
    public boolean enqueue(PQElement<E> epqElement) {
        for(int i = 0; i < this.size; ++i){
            if(this.data[i].data.equals(epqElement.data))
                throw new IllegalArgumentException("Element is already in the heap.");
        }
        if(this.size + 1 > data.length) {
            PQElement[] temp = new PQElement[this.data.length * 2];
            for (int i = 0; i < data.length; i++){
                temp[i] = data[i];
            }
            data = temp;
        }
        data[this.size] = epqElement;
        int idx = this.size;
        percolateUp(idx);
        this.size++;
        return true;
    }

    @Override
    public PQElement<E> dequeue() {
        if(this.size <= 0)
            return null;
        else if (this.size == 1){
            PQElement<E> temp = this.data[0];
            this.data[0] = null;
            this.size--;
            return temp;
        } else{
            PQElement<E> temp = this.data[0];
            this.data[0] = this.data[this.size-1];
            this.data[this.size-1] = null;
            int idx = 0;
            percolateDown(idx);
            this.size--;
            return temp;
        }
    }

    @Override
    public PQElement<E> peek() {
        return this.data[0];
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<PQElement<E>> iterator() {
        return null;
    }

    private void swap(int i, int j){
        PQElement temp = this.data[i];
        this.data[i] = this.data[j];
        this.data[j] = temp;
    }

    private void percolateDown(int idx){
        while((idx+1)*4 <= this.data.length-1 && this.data[(idx+1)*4-3] != null) {
            int p = Integer.MAX_VALUE;
            int index = 0;
            for(int i = (idx + 1) * 4 - 3; i <= (idx + 1) * 4; ++i){
                if(this.data[i] == null)
                    break;
                if(this.data[i].priority < p){
                    p = (int) this.data[i].priority;
                    index = i;
                }
            }
            if(this.data[idx].priority > p){
                swap(idx, index);
                idx = index;
            }
            else
                break;
        }
    }

    private void percolateUp(int idx){
        while(((idx -1) / 4) >= 0 && data[((idx -1) / 4)].priority > data[idx].priority) {
            if(this.data[idx].priority < this.data[(idx-1)/4].priority){
                swap(idx, (idx-1)/4);
                idx = ((idx -1) / 4);
            }
            else {
                break;
            }
        }
    }
}