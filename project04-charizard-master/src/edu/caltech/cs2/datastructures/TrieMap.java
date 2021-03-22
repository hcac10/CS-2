package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ITrieMap;

import java.util.*;
import java.util.function.Function;

public class TrieMap<A, K extends Iterable<A>, V> implements ITrieMap<A, K, V> {
    private TrieNode<A, V> root;
    private Function<IDeque<A>, K> collector;
    private int size;

    public TrieMap(Function<IDeque<A>, K> collector) {
        this.root = new TrieNode<>();
        this.collector = collector;
        this.size = 0;
    }

    @Override
    public V remove(K key) {
        V answer = get(key);
        Iterator<A> letters = key.iterator();
        TrieNode<A, V> current = this.root;
        removeHelp(current, letters);
        if(answer != null) {
            this.size--;
        }
        return answer;
    }
    private boolean removeHelp(TrieNode<A, V> current, Iterator<A> letters) {
        if(current == null)
            return false;
        if(!letters.hasNext()) {
            current.value = null;
            if(current.pointers.size() == 0){
                current = null;
                return true;
            }
            else{
                return false;
            }
        } else {
            A a = letters.next();
            boolean b = removeHelp(current.pointers.get(a), letters);
            if (b) {
                current.pointers.remove(a);
            }
            if (current.pointers.size() == 0 && current.value == null) {
                current = null;
                return true;
            }
        }
        return false;
    }

    public void clear(){
        this.root = new TrieNode<>();
        this.size = 0;
    }

    @Override
    public boolean isPrefix(K key) {
        return isIn(key) != null;
    }

    private TrieNode<A, V> isIn(K key){
        TrieNode<A, V> current = this.root;
        for (A c : key ){
            //
            if(current.pointers.keySet().contains(c)){
                current = current.pointers.get(c);
            }
            else
                return null;
        }
        return current;
    }

    @Override
    public IDeque<V> getCompletions(K prefix) {
        if(!isPrefix(prefix)) {
            return new ArrayDeque<>();
        }
        Iterator<A> letters = prefix.iterator();
        TrieNode<A, V> current = this.root;
        while(letters.hasNext()){
            A temp = letters.next();
            if(current.pointers.keySet().contains(temp)){
                current = current.pointers.get(temp);
            }
            else
                return new ArrayDeque<>();
        }
        IDeque<V> ans = new ArrayDeque<>();
        helpValues(current, ans);
//        helpCompletions(current, letters, ans);
        return ans;
    }

    @Override
    public V get(K key) {
        Iterator<A> letters = key.iterator();
        TrieNode<A, V> current = this.root;
        while(letters.hasNext()){
            A temp = letters.next();
            if(current.pointers.keySet().contains(temp)){
                current = current.pointers.get(temp);
            }
            else
                return null;
        }
        return current.value;
    }

    @Override
    public V put(K key, V value) {
        Iterator<A> letters = key.iterator();
        TrieNode<A, V> current = this.root;
//        boolean increase = false;
        while(letters.hasNext()){
            A temp = letters.next();
            if(current.pointers.keySet().contains(temp)){
                current = current.pointers.get(temp);
            }
            else {
//                increase = true;
                current.pointers.put(temp, new TrieNode<A, V>());
                current = current.pointers.get(temp);
            }
        }
        V val = current.value;
        if(val == null) {
            this.size++;
        }
        //Add test for if value is null later
        current.value = value;
        return val;
    }

    @Override
    public boolean containsKey(K key) {
        Iterator<A> letters = key.iterator();
        TrieNode<A, V> current = this.root;
        while(letters.hasNext()){
            A temp = letters.next();
            if(current.pointers.keySet().contains(temp)){
                current = current.pointers.get(temp);
            }
            else{
                return false;
            }
        }
        return current.value != null;
    }

    @Override
    public boolean containsValue(V value) {
        return containsV(value, this.root);
    }

    private boolean containsV(V value, TrieNode<A, V> curr){
        if(curr.value != null && curr.value.equals(value))
            return true;
        for(A c: curr.pointers.keySet()){
            if(containsV(value, curr.pointers.get(c)))
                return true;
        }
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keySet() {
        ICollection<K> ans = new ArrayDeque<K>();
        IDeque<A> temp = new ArrayDeque<>();
        keySet(this.root, ans, temp);
        return ans;
    }

    private void keySet(TrieNode<A, V> current, ICollection<K> result, IDeque<A> inp){
        if(current.value != null) {
            result.add(this.collector.apply(inp));
        }
        for(A c: current.pointers.keySet()){
            inp.addBack(c);
            keySet(current.pointers.get(c), result, inp);
            inp.removeBack();
        }
    }

    @Override
    public ICollection<V> values() {
        ICollection<V> ans = new ArrayDeque<>();
        helpValues(this.root, ans);
        return ans;
    }

    private void helpValues(TrieNode<A, V> current, ICollection<V> inp){
        if(current == null)
            return;
        if(current.value != null)
            inp.add(current.value);
        for(A c: current.pointers.keySet()){
            helpValues(current.pointers.get(c), inp);
        }
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
    
    private static class TrieNode<A, V> {
        public final Map<A, TrieNode<A, V>> pointers;
        public V value;

        public TrieNode() {
            this(null);
        }

        public TrieNode(V value) {
            this.pointers = new HashMap<>();
            this.value = value;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            if (this.value != null) {
                b.append("[" + this.value + "]-> {\n");
                this.toString(b, 1);
                b.append("}");
            }
            else {
                this.toString(b, 0);
            }
            return b.toString();
        }

        private String spaces(int i) {
            StringBuilder sp = new StringBuilder();
            for (int x = 0; x < i; x++) {
                sp.append(" ");
            }
            return sp.toString();
        }

        protected boolean toString(StringBuilder s, int indent) {
            boolean isSmall = this.pointers.entrySet().size() == 0;

            for (Map.Entry<A, TrieNode<A, V>> entry : this.pointers.entrySet()) {
                A idx = entry.getKey();
                TrieNode<A, V> node = entry.getValue();

                if (node == null) {
                    continue;
                }

                V value = node.value;
                s.append(spaces(indent) + idx + (value != null ? "[" + value + "]" : ""));
                s.append("-> {\n");
                boolean bc = node.toString(s, indent + 2);
                if (!bc) {
                    s.append(spaces(indent) + "},\n");
                }
                else if (s.charAt(s.length() - 5) == '-') {
                    s.delete(s.length() - 5, s.length());
                    s.append(",\n");
                }
            }
            if (!isSmall) {
                s.deleteCharAt(s.length() - 2);
            }
            return isSmall;
        }
    }
}
