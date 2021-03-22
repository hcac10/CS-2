package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;

public class MoveToFrontDictionary<K, V> implements IDictionary<K,V> {
    private int size;
    private Node head;


    private class Node{
        K key;
        V value;
        public Node next;
        public Node previous;


        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
            this.previous = null;
        }
    }
    public MoveToFrontDictionary() {
        this.head = null;
        this.size = 0;

    }

    public V moveToFront(K key){
        Node front = this.head;
        if (this.size == 0){
            return null;
        }
        if (front.value == null){
            return null;
        }
        while (front != null) {
            if (front.key.equals(key)){
                if (front.key.equals(this.head.key)){
                    return front.value;
                }
                V value = front.value;
                Node after = front.next;
                Node before = front.previous;
                if (before != null){
                    before.next = after;
                }
                if (after != null){
                    after.previous = before;
                }
                Node helper = this.head;
                this.head = front;
                this.head.next = helper;
                this.head.previous = null;
                helper.previous = this.head;
                return front.value;
            }
            front = front.next;
        }
        return null;
    }


    @Override
    public V remove(K key) {
        if (moveToFront(key)!= null) {
            V value = moveToFront(key);
            this.head = this.head.next;
            if (this.head != null) {
                this.head.previous = null;
            }
            size --;
            return value;
        }
        return null;
    }


    @Override
    public V put(K key, V value) {
        if (size == 0) {
            Node newhead = new Node(key, value);
            this.head = newhead;
            size ++;
            return null;
        }
        if (containsKey(key)){
            V front = moveToFront(key);
            this.head.value = value;
            return front;
        }
        Node newhead = new Node(key,value);
        Node oldhead = this.head;
        newhead.next = oldhead;
        oldhead.previous = newhead;
        this.head = newhead;
        size ++;
        return null;

    }
    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        return values().contains(value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public ICollection<K> keySet() {
        LinkedDeque<K> keys = new LinkedDeque<>();
        if (this.size != 0){
            Node node = this.head;
            while (node != null){
                keys.addBack(node.key);
                node = node.next;
            }
        }
        return keys;
    }

    @Override
    public ICollection<V> values() {
        LinkedDeque<V> values = new LinkedDeque<>();
        int counter = 0;
        if (this.size!= 0){
            Node node = this.head;
            while(node != null){
                values.addBack(node.value);
                node = node.next;
                counter++;
            }
        }
        return values;
    }

    public V get(K key) {
        return moveToFront(key);
    }


    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}