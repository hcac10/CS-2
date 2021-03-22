package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.*;

public class Graph<V, E> extends IGraph<V, E> {
    private ChainingHashDictionary<V, ChainingHashDictionary<V, E>> graph;

    public Graph(){
        graph = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }
    @Override
    public boolean addVertex(V vertex) throws IllegalArgumentException{
        if(graph.containsKey(vertex)){
            return false;
        }
        else {
            graph.put(vertex, new ChainingHashDictionary<>(MoveToFrontDictionary::new));
            return true;
        }
    }

    @Override
    public boolean addEdge(V src, V dest, E e) {
        if(!graph.containsKey(src) || !graph.containsKey(dest)){
            throw new IllegalArgumentException("You can't add an edge");
        }
        boolean check = true;
        if(graph.get(src).containsKey(dest)){
            check = false;
        }
        graph.get(src).put(dest, e);
        return check;

    }

    @Override
    public boolean addUndirectedEdge(V n1, V n2, E e) {
        if(!graph.containsKey(n1) || !graph.containsKey(n2)){
            throw new IllegalArgumentException("You can't add an edge");
        }
        return addEdge(n1, n2, e) && addEdge(n2, n1, e);
    }

    @Override
    public boolean removeEdge(V src, V dest) {
        if(!graph.containsKey(src) || !graph.containsKey(dest)){
            throw new IllegalArgumentException("You can't add an edge");
        }
        if(graph.get(src).containsKey(dest)){
            graph.get(src).remove(dest);
            return true;
        }
        return false;
    }

    @Override
    public ISet<V> vertices() {
        ChainingHashSet<V> fin = new ChainingHashSet<>();
        for(V cycle: graph.keySet()){
            fin.add(cycle);
        }
        return fin;
    }

    @Override
    public E adjacent(V i, V j) {
        if(!graph.containsKey(i) || !graph.containsKey(j)){
            throw new IllegalArgumentException("You can't add an edge");
        }
        if(graph.get(i).containsKey(j)){
            return graph.get(i).get(j);
        }
        return null;
    }

    @Override
    public ISet<V> neighbors(V vertex) {
        ChainingHashSet<V> fin = new ChainingHashSet<>();
        if(!graph.containsKey(vertex)){
            throw new IllegalArgumentException("You can't add an edge");
        }
        for(V cycle: graph.get(vertex)){
            fin.add(cycle);
        }
        return fin;
    }
}