package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IFixedSizeQueue;

import java.util.Iterator;

public class CircularArrayFixedSizeQueue<E> implements IFixedSizeQueue<E> {
    private E[] a;
    private int front = 0;
    int back = 0;
    private int size;


    public CircularArrayFixedSizeQueue(int initialCapacity){
        this.a = (E[]) new Object[initialCapacity];
        this.size = 0;
    }


    @Override
    public boolean isFull() {
        if(size == a.length){
            return true;
        }
        return false;
    }

    @Override
    public int capacity() {
        return this.a.length;
    }

    @Override
    public boolean enqueue(E e) {
        if(!isFull()){
            if(!(this.size == 0))
                back = back + 1;
            if(back == a.length){
                back = 0;
            }

            a[back] = e;
            size++;
            return true;
        }
        return false;

    }

    @Override
    public E dequeue() {
        if (size <= 0){
            return null;
        }
        E temp = a[front];
        if(!(front == back && size == 1))
            front = front + 1;
        if(front == a.length){
            front = 0;
        }
        size--;
        return temp;
    }

    @Override
    public E peek() {
        if (size <= 0){
            return null;
        }
        return a[front];
    }

    @Override
    public int size() {
        return size;
    }

    public class CircularArrayFixedSizeQueueIterator implements Iterator<E>{
        private int ind;

        public CircularArrayFixedSizeQueueIterator(){
            this.ind = 0;
        }

        @Override
        public boolean hasNext(){
            return this.ind < CircularArrayFixedSizeQueue.this.size;
        }
        @Override
        public E next(){
            E e = CircularArrayFixedSizeQueue.this.a[this.ind];
            this.ind++;
            return e;
        }
    }
    @Override
    public Iterator<E> iterator() {
        return new CircularArrayFixedSizeQueueIterator();
    }
    @Override
    public String toString() {
        String var = "[";
        if (this.size == 0) {
            return "[]";
        }
        /*
        for(int i = this.front; i < this.back; i = (i + 1) % a.length){
            if(i != this.back - 1)
                var += a[i] + ", ";
            else
                var += a[i] + "]";
        }
        return var;

         */


        for (int i = 0; i < this.size; i++) {
            if (i + front < this.a.length) {
                var += a[i + front] + ", ";
            } else if (i + front >= this.a.length) {
                var += a[i % capacity()] + ", ";
            }
        }
    return var.substring(0, var.length() - 2) + "]";
    }
}
