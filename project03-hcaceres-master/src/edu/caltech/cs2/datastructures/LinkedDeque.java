package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class LinkedDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;

    private static class Node<E>{
        public final E dat;
        public Node<E> next;
        public Node<E> prev;

        public Node(E dat, Node<E> next, Node<E> prev){
            this.dat = dat;
            this.next = next;
            this.prev = prev;
        }

        public Node(E dat){
            this(dat, null, null);
        }

    }
    public LinkedDeque(){
        this.size = 0;
    }

    @Override
    public void addFront(E e) {
        if(size == 0){
            this.head = new Node<>(e);
            this.tail = this.head;
        }
        else{
            this.head = new Node<>(e, this.head, null);
            this.head.next.prev = this.head;
        }
        size++;
    }

    @Override
    public void addBack(E e) {
        if(size == 0){
            this.head = new Node<>(e);
            this.tail = this.head;
        }
        else{
            this.tail = new Node<>(e, null, this.tail);
            this.tail.prev.next = this.tail;
        }
        size++;
    }

    @Override
    public E removeFront() {
        if(size <= 0){
            return null;
        }
        E dat = this.head.dat;
        this.head = this.head.next;
        if(this.head == null){
            this.tail = null;
        }
        else{
            this.head.prev = null;
        }
        size--;
        return dat;
    }

    @Override
    public E removeBack() {
        if(size <= 0){
            return null;
        }
        E dat = this.tail.dat;
        this.tail = this.tail.prev;
        if(this.tail == null){
            this.head = null;
        }
        else{
            this.tail.next = null;
        }
        size--;
        return dat;

    }

    @Override
    public boolean enqueue(E e) {
        addFront(e);
        return true;
    }

    @Override
    public E dequeue() {
        return removeBack();
    }

    @Override
    public boolean push(E e) {
        addBack(e);
        return true;
    }

    @Override
    public E pop() {
        return removeBack();
    }

    @Override
    public E peek() {
        if(this.tail == null){
            return null;
        }
        return this.tail.dat;
    }

    @Override
    public E peekFront() {
        if(this.head == null){
            return null;
        }
        return this.head.dat;
    }

    @Override
    public E peekBack() {
        if(this.tail == null){
            return null;
        }
        return this.tail.dat;
    }

    public class LinkedDequeIterator implements Iterator<E>{
        private Node<E> ind;

        public LinkedDequeIterator(){
            this.ind = LinkedDeque.this.head;
        }

        @Override
        public boolean hasNext(){
            return this.ind != null;
        }
        @Override
        public E next(){
            E e = this.ind.dat;
            this.ind = this.ind.next;
            return e;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedDeque.LinkedDequeIterator();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString(){
        String var = "[";
        Node<E> temp = this.head;
        for(int i = 0; i < size; i++){
            var += temp.dat + ", ";
            temp = temp.next;
        }
        if(this.head == null){
            return "[]";
        }

        return var.substring(0, var.length() - 2) + "]";
    }
}


