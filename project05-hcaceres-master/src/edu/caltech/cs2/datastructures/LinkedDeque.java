package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class LinkedDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {

    private int size;
    private Node head;
    private Node tail;

    private class Node{
        Node next;
        Node prev;
        E data;

        public Node(E data){
            this.data = data;
            next = null;
            prev = null;
        }
    }

    public LinkedDeque(){
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    public String toString(){
        if(this.size == 0)
            return "[]";
        else{
            String ans = "[";
            Node temp = this.head;
            for(int i = 0; i < this.size-1; ++i){
                ans += temp.data + ", ";
                temp = temp.next;
            }
            ans += temp.data + "]";
            return ans;
        }
    }

    @Override
    public void addFront(E e) {
        if(this.size == 0){
            this.head = new Node(e);
            this.tail = this.head;
            this.size++;
            return;
        }
        Node temp = new Node(e);
        Node other = this.head;
        temp.next = other;
        other.prev = temp;
        this.head = temp;
        this.size++;

    }

    @Override
    public void addBack(E e) {
        if(this.size == 0) {
            this.head = new Node(e);
            this.tail = this.head;
            this.size++;
            return;
        }else {
            Node ans = new Node(e);
            Node temp = this.tail;
            temp.next = ans;
            this.tail = ans;
            this.tail.prev = temp;
            this.size++;
        }
    }

    @Override
    public E removeFront() {
        if(this.size <= 0)
            return null;
        else{
            E ans = this.head.data;
            this.head = this.head.next;
            this.size--;
            return ans;
        }
    }

    @Override
    public E removeBack() {
        if(this.size <= 0)
            return null;
        if(this.size == 1){
            E ans = this.head.data;
            this.head = null;
            this.tail = null;
            this.size--;
            return ans;
        }
        else {
            E ans = this.tail.data;
            this.tail = this.tail.prev;
            this.tail.next = null;
            this.size--;
            return ans;
//            Node curr = this.head;
//            Node currNext = curr.next;
//            while (currNext.next != null){
//                curr = currNext;
//                currNext = currNext.next;
//            }
//            E ans = curr.data;
//            currNext = null;
//            this.tail = curr;
//            this.size--;
//            return ans;
        }
    }

    @Override
    public boolean enqueue(E e) {
        addFront(e);
        return true;
    }

    @Override
    public E dequeue() {
        if(this.size == 0)
            return null;
        else{
            E ans = this.tail.data;
            removeBack();
            return ans;
        }
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
        if(size <= 0)
            return null;
        return this.tail.data;
    }

    @Override
    public E peekFront() {
        if(this.size <= 0)
            return null;
        return this.head.data;
    }

    @Override
    public E peekBack() {
        if(this.size <= 0)
            return null;
        else
            return this.tail.data;
    }

    public class LinkedDequeIterator implements Iterator<E> {
        private int ind;

        public boolean hasNext(){
            return this.ind < LinkedDeque.this.size;
        }

        public E next() {
            Node temp = head;
            for(int i = 0; i < ind; ++i)
                temp = temp.next;
            E e = temp.data;
            this.ind++;
            return e;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedDequeIterator();
    }

    @Override
    public int size() {
        return this.size;
    }
}
