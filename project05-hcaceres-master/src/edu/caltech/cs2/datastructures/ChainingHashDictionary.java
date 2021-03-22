package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;
import java.util.function.Supplier;

public class ChainingHashDictionary<K, V> implements IDictionary<K, V> {
    private Supplier<IDictionary<K, V>> chain;
    private Object[] hashTable;
    private int [] sizes = {41, 83, 137, 241, 431, 883, 1667, 3271, 6473, 12281, 25457, 52511, 105269, 207443, 397567};
    private int ind;
    private int size;
    private int length;
    private double loadFactor;

    public ChainingHashDictionary(Supplier<IDictionary<K, V>> chain) {
        hashTable = new Object[31];
        ind = 0;
        this.size = 0;
        this.length = 0;
        this.chain = chain;
        loadFactor = 0.75;
    }
    public void rehash(){
        length ++;
        Object[] temp = new Object[sizes[ind]];
        ind++;
        for (Object i : hashTable){
            if (i != null){
                for (K key : ((IDictionary<K,V>)i).keySet()){
                    int rehash = key.hashCode() % temp.length;
                    if(temp[rehash]==null){
                        temp[rehash] = chain.get();
                    }
                    IDictionary<K,V> redo = (IDictionary<K,V>) temp[rehash];
                    redo.put(key,((IDictionary<K,V>)i).get(key));
                }
            }
        }
        hashTable = temp;
    }
    /**
     * @param key
     * @return value corresponding to key
     */
    @Override
    public V get(K key) {
        int hashtime = key.hashCode() % hashTable.length;
        if (hashTable[hashtime]==null){
            return null;
        }
        IDictionary<K,V> temp = (IDictionary<K,V>)hashTable[hashtime];
        return temp.get(key);
    }

    @Override
    public V remove(K key) {
        int hashspot = key.hashCode() % hashTable.length;
        IDictionary<K,V> helper = (IDictionary<K,V>)hashTable[hashspot];
        if (helper == null) {
            return null;
        }
        V value = helper.remove(key);
        if (value != null){
            size--;
        }
        return value;
    }

    @Override
    public V put(K key, V value) {
        if ((double)(size+1)/hashTable.length > loadFactor){
            rehash();
        }
        int hashspot = key.hashCode() % hashTable.length;
        if (hashTable[hashspot] == null){
            hashTable[hashspot] = chain.get();
        }
        V value1 = ((IDictionary<K,V>)hashTable[hashspot]).get(key);
        if (value1 == null){
            size++;
        }
        ((IDictionary<K,V>)hashTable[hashspot]).put(key,value);
        return value1;
    }

    @Override
    public boolean containsKey(K key) {
        if (get(key) == null){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * @param value
     * @return true if the HashDictionary contains a key-value pair with
     * this value, and false otherwise
     */
    @Override
    public boolean containsValue(V value) {
        return values().contains(value);
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
        LinkedDeque<K> keys = new LinkedDeque<>();
        for (Object o : hashTable) {
            if (o != null) {
                IDictionary<K, V> helper = (IDictionary<K, V>) o;
                for (K key : helper.keySet()) {
                    keys.addBack(key);
                }
            }
        }
        return keys;
    }

    @Override
    public ICollection<V> values() {
        LinkedDeque<V> values = new LinkedDeque<>();
        for (Object o : hashTable) {
            if (o != null) {
                IDictionary<K, V> helper = (IDictionary<K, V>) o;
                for (V value : helper.values()) {
                    values.addBack(value);
                }
            }
        }
        return values;
    }

    /**
     * @return An iterator for all entries in the HashDictionary
     */
    @Override
    public Iterator<K> iterator() {
        return this.keySet().iterator();
    }


}