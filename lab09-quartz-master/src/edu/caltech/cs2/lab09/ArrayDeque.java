package edu.caltech.cs2.lab09;

import java.util.Iterator;

public class ArrayDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {
    private E[] vals;
    private int size;
    private static final int growFactor = 2;
    private static final int defaultCapacity = 10;


    public ArrayDeque(){
        this(defaultCapacity);
    }

    public ArrayDeque(int initialCapacity){
        this.vals = (E[]) new Object[initialCapacity];
        this.size = 0;
    }

    public String toString(){
        if(this.size == 0)
            return "[]";
        String str = "[";
        for(int i = 0; i < this.size - 1; ++i)
            str += vals[i]+", ";
        str += vals[this.size-1] + "]";
        return str;
    }

    @Override
    public void addFront(E e) {
        if(this.size != this.vals.length){
            E[] temp = (E[]) new Object[this.vals.length];
            for(int i = 0; i < temp.length; ++i)
                temp[i] = this.vals[i];
            this.vals[0] = e;
            for(int i = 0; i < size; ++i){
                this.vals[i+1] = temp[i];
            }
            this.size++;
        }
        else {
            E[] temp = (E[]) new Object[this.vals.length];
            for(int i = 0; i < temp.length; ++i)
                temp[i] = this.vals[i];
            this.vals = (E[]) new Object[this.vals.length * growFactor];
            this.vals[0] = e;
            for(int i = 0; i < temp.length; ++i)
                this.vals[i+1] = temp[i];
            this.size++;
        }
    }

    @Override
    public void addBack(E e) {
        if(this.size == 0){
            this.vals[0] = e;
            this.size = 1;
            return;
        }
        if(this.size < this.vals.length){
            this.vals[this.size] = e;
            this.size++;
        }
        else{
            E[] temp = (E[]) new Object[this.vals.length * growFactor];
            for(int i = 0; i < this.vals.length; ++i)
                temp[i] = this.vals[i];
            temp[this.size] = e;
            this.vals = temp;
            this.size++;
        }
    }

    @Override
    public E removeFront() {
        if(this.size <= 0)
            return null;
        else {
            E ans = this.vals[0];
            E[] temp = (E[]) new Object[this.size];
            for(int i = 0; i < this.size-1; ++i)
                temp[i] = this.vals[i+1];
            this.size--;
            this.vals = temp;
            return ans;
        }
    }

    @Override
    public E removeBack() {
        if(this.size <= 0)
            return null;
        else {
            E ans = this.vals[this.size-1];
            E[] temp = (E[]) new Object[this.size];
            for(int i = 0; i < this.size-1; ++i)
                temp[i] = this.vals[i];
            this.vals = temp;
            this.size--;
            return ans;
        }
    }

    @Override
    public boolean enqueue(E e) {
        if(this.size > this.vals.length)
            return false;
        else{
            addFront(e);
//            this.size++;
            return true;
        }
    }

    @Override
    public E dequeue() {
        if(this.size == 0)
            return null;
        else{
            E ans = this.vals[this.size-1];
            removeBack();
//            this.size--;
            return ans;
        }
    }

    @Override
    public boolean push(E e) {
        if(this.size > this.vals.length)
            return false;
        else{
            addBack(e);
//            this.size++;
            return true;
        }
    }

    @Override
    public E pop() {
        if(this.size <= 0)
            return null;
        else{
            E ans = this.vals[this.size-1];
//            removeBack();
            size--;
            return ans;
        }
    }

    @Override
    public E peek() {
        if(this.size <= 0)
            return null;
        else{
            return this.vals[this.size-1];
        }
//        return peekFront();
//        if(this.size <= 0)
//            return null;
//        else
//            return this.vals[this.size-1];
    }

    @Override
    public E peekFront() {
        if(this.size <= 0)
            return null;
        return this.vals[0];
    }

    @Override
    public E peekBack() {
        if(this.size <= 0)
            return null;
        else{
            return this.vals[this.size-1];
        }
    }

    public class ArrayDequeIterator implements Iterator<E> {
        private int ind;

        public boolean hasNext(){
            return this.ind < ArrayDeque.this.size;
        }

        public E next() {
            E e = ArrayDeque.this.vals[ind];
            this.ind++;
            return e;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public int size() {
        return this.size;
    }
}

