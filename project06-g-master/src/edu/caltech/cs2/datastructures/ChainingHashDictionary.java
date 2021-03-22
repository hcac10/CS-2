package edu.caltech.cs2.datastructures;

import com.sun.net.httpserver.Filter;
import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IQueue;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ChainingHashDictionary<K, V> implements IDictionary<K, V> {
    private Supplier<IDictionary<K, V>> chain;
    private IDictionary[] data;
    private int size;

    public ChainingHashDictionary(Supplier<IDictionary<K, V>> chain) {
        this.chain = chain;
        this.data = new IDictionary[31];
        for(int i = 0; i < this.data.length; ++i){
            this.data[i] = chain.get();
        }
        this.size = 0;
    }

    /**
     * @param key
     * @return value corresponding to key
     */
    @Override
    public V get(K key) {
        int ind = getHash(key, this.data.length);
        if(this.data[ind].size() == 0)
            return null;
        return (V) this.data[ind].get(key);
    }

    @Override
    public V remove(K key) {
        if(!containsKey(key))
            return null;
        int ind = getHash(key, this.data.length);
        if(this.data[ind].size() == 0)
            return null;
        V ans = (V) this.data[ind].get(key);
        this.data[ind].remove(key);
        this.size--;
        return ans;
    }

    @Override
    public V put(K key, V value) {
        int ind = getHash(key, this.data.length);
        V ans = null;
        if(this.data[ind].containsKey(key)){
            ans = (V) this.data[ind].get(key);
            this.size--;
        }
        this.data[ind].put(key, value);
        this.size++;
        rehash();
        return ans;
    }

    @Override
    public boolean containsKey(K key) {
        return this.data[getHash(key, this.data.length)].containsKey(key);
    }

    /**
     * @param value
     * @return true if the HashDictionary contains a key-value pair with
     * this value, and false otherwise
     */
    @Override
    public boolean containsValue(V value) {
        for(int i = 0; i < this.data.length; ++i)
            if(this.data[i].values().contains(value))
                return true;
        return false;
    }

    /**
     * @return number of key-value pairs in the HashDictionary
     */
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keySet() {
        IDeque<K> ans = new ArrayDeque<>();
        for(int i = 0; i < this.data.length; ++i){
            for(Object key: this.data[i].keySet()){
                ans.addBack((K) key);
            }
        }
        return ans;
    }

    @Override
    public ICollection<V> values() {
        IDeque<V> ans = new ArrayDeque<>();
        for(int i = 0; i < this.data.length; ++i){
            for(Object key: this.data[i].keySet()){
                ans.addBack((V) this.data[i].get(key));
            }
        }
        return ans;
    }

    /**
     * @return An iterator for all entries in the HashDictionary
     */
    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    private int getHash(K key, int size){
        int ans = key.hashCode() % size;
        while(ans < 0)
            ans += size;
        return ans;
    }

    private void rehash(){
        double loadFactor = this.size / this.data.length;
        if(loadFactor <= 1.25)
            return;
        IDictionary[] other = new IDictionary[nextPrime()];
        for(int i = 0; i < other.length; ++i)
            other[i] = this.chain.get();
        for(int i = 0; i < this.data.length; ++i){
            for(Object key: this.data[i].keySet()){
                K val = (K) key;
                other[getHash(val, other.length)].put(val, this.data[i].get(val));
            }
        }
        this.data = other;
    }

    private int nextPrime(){
        int next = this.size()+1;
        boolean isPrime = false;
        boolean needToContinue = false;
        while(!isPrime){
            for(int i = 2; i < Math.sqrt(next); ++i) {
                if (next % i == 0) {
                    needToContinue = true;
                    break;
                }
            }
            if(needToContinue){
                next++;
                needToContinue = false;
                continue;
            }
            isPrime = true;
        }
        return next;
    }
}
