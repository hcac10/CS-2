package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class ArrayDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {
    private E[] a;
    private int size;
    private static final int capacity = 10;
    private static final int fact = 2;

    public ArrayDeque(){
       this(capacity);
    }

    public ArrayDeque(int initialCapacity){
        this.a = (E[]) new Object[initialCapacity];
        this.size = 0;
    }

    public void addFront(E e) {
        if(size + 1 > a.length) {
            E[] hold = (E[]) new Object[a.length * fact];
            for (int i = 0; i < a.length; i++) {
                hold[i] = a[i];
            }
            a = hold;
        }
        for (int i = size; i > 0; i--){
            a[i] = a[i-1];
        }
        a[0] = e;
        size++;
    }

    @Override
    public void addBack(E e) {
        if(size + 1 > a.length) {
            E[] hold = (E[]) new Object[a.length * fact];
            for (int i = 0; i < a.length; i++) {
                hold[i] = a[i];
            }
            a = hold;
        }
        a[size] = e;
        size++;
    }

    @Override
    public E removeFront() {
        if(size <= 0){
            return null;
        }
        E var = a[0];
        for(int i = 0; i < size - 1; i++){
            a[i] = a[i+1];
        }
        size--;
        return var;
    }

    @Override
    public E removeBack() {
        if(size <= 0){
            return null;
        }
        size--;
        return a[size];
    }

    @Override
    public boolean enqueue(E e) {
        addFront(e);
        return true;
    }

    @Override
    public E dequeue() {
        if (size <= 0){
            return null;
        }
        size--;
        return a[size];
    }

    @Override
    public boolean push(E e) {
        addBack(e);
        return true;
    }

    @Override
    public E pop() {
        if (size<= 0){
            return null;
        }
        size--;
        return a[size];
    }

    @Override
    public E peek() {
        if (size<= 0) {
            return null;
        }
        return a[size-1];
    }

    @Override
    public E peekFront() {
        if (size <= 0) {
            return null;
        }
        return a[0];
    }

    @Override
    public E peekBack() {
        if (size<= 0) {
            return null;
        }
        return a[size-1];
    }

    public class ArrayDequeIterator implements Iterator<E>{
        private int ind;

        public ArrayDequeIterator(){
            this.ind = 0;
        }

        @Override
        public boolean hasNext(){
            return this.ind < ArrayDeque.this.size;
        }
        @Override
        public E next(){
            E e = ArrayDeque.this.a[this.ind];
            this.ind++;
            return e;
        }
    }
    public Iterator<E> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public String toString(){
        String var = "[";
        for(int i = 0; i < size; i++){
            var += a[i] + ", ";
        }
        if(var.length() < 2){
            return "[]";
        }

        return var.substring(0, var.length() - 2) + "]";
    }
}

