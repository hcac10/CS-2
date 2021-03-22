package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MoveToFrontDictionary<K, V> implements IDictionary<K,V> {
    private Node head;
    private Node tail;
    private int size;

    private class Node{
        Node next;
        Node prev;
        V data;
        K key;

        public Node(V data, K key){
            this.data = data;
            this.next = null;
            this.prev = null;
            this.key = key;
        }
    }

    public MoveToFrontDictionary() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public V remove(K key) {
        if(moveToFront(key) == null)
            return null;
        else{
            moveToFront(key);
            V ans = this.head.data;
            Node temp = this.head.next;
            this.head = null;
            this.head = temp;
            if(this.head != null)
                this.head.prev = null;
            this.size--;
            return ans;
        }
    }

    @Override
    public V put(K key, V value) {
        V ans = null;
        if(moveToFront(key) != null){
            ans = this.head.data;
            this.head.data = value;
        } else{
            Node temp = new Node(value, key);
            Node other = this.head;
            temp.next = other;
            if(!(other == null))
                other.prev = temp;
            this.head = temp;
            this.size++;
        }
        return ans;
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
        return this.size;
    }

    @Override
    public ICollection<K> keySet() {
        IDeque<K> ans = new ArrayDeque<>();
        if(this.size == 0)
            return new ArrayDeque<>();
        Node current = this.head;
        while(current != null){
            ans.add(current.key);
            current = current.next;
        }
        return ans;
    }

    @Override
    public ICollection<V> values() {
        IDeque<V> ans = new ArrayDeque<>();
        if(this.size == 0)
            return new ArrayDeque<>();
        Node current = this.head;
        while(current != null){
            ans.addBack(current.data);
            current = current.next;
        }
        return ans;
    }

    public V get(K key) {
        if(moveToFront(key) == null){
            return null;
        }else{
            return this.head.data;
        }
    }

    public K moveToFront(K key){
        if(this.size == 0)
            return null;
        Node current = this.head;
        while(current != null){
            if(current.key.equals(key)){
                if(current.key.equals(this.head.key)){
                    return key;
                }
                Node previous = current.prev;
                Node after = current.next;
                if(after == null){
                    this.tail = previous;
                }
                if(previous != null){
                    previous.next = after;
                    if(after != null)
                        after.prev = previous;
                }
                Node temp = this.head;
                this.head = current;
                this.head.next = temp;
                if(temp != null)
                    temp.prev = this.head;
                return key;
            }
            current = current.next;
        }
        return null;
    }

    public class MoveToFrontDictionaryIterator implements Iterator<K> {
        private int ind;
        private Node val;

        public MoveToFrontDictionaryIterator(){
            val = MoveToFrontDictionary.this.head;
            ind = 0;
        }

        public boolean hasNext(){
            return this.ind < MoveToFrontDictionary.this.size;
        }

        public K next() {
            K e = val.key;
            val = val.next;
            this.ind++;
            return e;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new MoveToFrontDictionaryIterator();
    }
}
