package edu.caltech.cs2.datastructures;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IPriorityQueue;
import edu.caltech.cs2.interfaces.ISet;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class BeaverMapsGraph extends Graph<Long, Double> {
    private static JsonParser JSON_PARSER = new JsonParser();

    private IDictionary<Long, Location> ids;
    private ISet<Location> buildings;

    public BeaverMapsGraph() {
        super();
        this.buildings = new ChainingHashSet<>();
        this.ids = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }

    /**
     * Reads in buildings, waypoinnts, and roads file into this graph.
     * Populates the ids, buildings, vertices, and edges of the graph
     * @param buildingsFileName the buildings filename
     * @param waypointsFileName the waypoints filename
     * @param roadsFileName the roads filename
     */
    public BeaverMapsGraph(String buildingsFileName, String waypointsFileName, String roadsFileName) {
        this();
        JsonElement bs = fromFile(buildingsFileName);
        for (JsonElement b : bs.getAsJsonArray()){
            Location loc = new Location(b.getAsJsonObject());
            this.addVertex(loc.id);
            buildings.add(loc);
            ids.put(loc.id, loc);
        }
        JsonElement ws = fromFile(waypointsFileName);
        for (JsonElement w : ws.getAsJsonArray()){
            Location loc = new Location(w.getAsJsonObject());
            this.addVertex(loc.id);
            ids.put(loc.id, loc);
        }
        JsonElement rs = fromFile(roadsFileName);
        for (JsonElement r : rs.getAsJsonArray()){
            for(int i = 0; i < r.getAsJsonArray().size()-1; i++) {
               JsonElement j1 = r.getAsJsonArray().get(i);
               JsonElement j2 = r.getAsJsonArray().get(i+1);
               Location l1 = ids.get(j1.getAsLong());
               Location l2 = ids.get(j2.getAsLong());
               this.addUndirectedEdge(l1.id, l2.id, l1.getDistance(l2));
            }
        }

    }

    /**
     * Returns a deque of all the locations with the name locName.
     * @param locName the name of the locations to return
     * @return a deque of all location with the name locName
     */
    public IDeque<Location> getLocationByName(String locName) {
        IDeque<Location> fin = new LinkedDeque<>();
        for(long i: ids.keySet()){
            if(locName.equals(ids.get(i).name)){
                fin.add(ids.get(i));
            }
        }
        return fin;
    }

    /**
     * Returns the Location object corresponding to the provided id
     * @param id the id of the object to return
     * @return the location identified by id
     */
    public Location getLocationByID(long id) {
        return ids.get(id);
    }

    /**
     * Adds the provided location to this map.
     * @param n the location to add
     * @return true if n is a new location and false otherwise
     */
    public boolean addVertex(Location n) {
        ids.put(n.id, n);
        return addVertex(n.id);
    }

    /**
     * Returns the closest building to the location (lat, lon)
     * @param lat the latitude of the location to search near
     * @param lon the longitute of the location to search near
     * @return the building closest to (lat, lon)
     */
    public Location getClosestBuilding(double lat, double lon) {
        Location fin = null;
        double distance = Double.MAX_VALUE;
        for (Location l: this.getBuildings()){
            if(l.getDistance(lat, lon) < distance) {
                distance = l.getDistance(lat, lon);
                fin = l;
            }
        }
        return fin;
    }

    /**
     * Returns a set of locations which are no more than threshold feet
     * away from start.
     * @param start the location to search around
     * @param threshold the number of feet in the search radius
     * @return
     */
    public ISet<Location> dfs(Location start, double threshold) {
        ISet<Location> fin = new ChainingHashSet<>();
        dfsHelp(start, threshold, start, fin);
        return fin;
    }
    private void dfsHelp(Location start, double threshold, Location cur, ISet<Location> fin){
        if(cur.getDistance(start) < threshold){
            if(!fin.contains(cur)){
                fin.add(cur);
                for(long i: this.neighbors(cur.id)){
                    dfsHelp(start, threshold, this.ids.get(i), fin);
                }
                return;
            }
        }
    }



    /**
     * Returns a list of Locations corresponding to
     * buildings in the current map.
     * @return a list of all building locations
     */
    public ISet<Location> getBuildings() {
        return this.buildings;
    }

    /**
     * Returns a shortest path (i.e., a deque of vertices) between the start
     * and target locations (including the start and target locations).
     * @param start the location to start the path from
     * @param target the location to end the path at
     * @return a shortest path between start and target
     */
    public IDeque<Location> dijkstra(Location start, Location target) {
        IDeque<Location> fin = new LinkedDeque<>();
        MinFourHeap<Location> lst = new MinFourHeap<>();
        lst.enqueue(new IPriorityQueue.PQElement<>(start, 0.0));
        IDictionary<Long, Long> parent = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
        IDictionary<Long, Double> priority = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
        priority.put(start.id, 0.0);
        while(lst.size() != 0) {
            IPriorityQueue.PQElement<Location> temp = lst.dequeue();
            if (temp.data.equals(target)) {
                break;
            } else {
                for (long id : this.neighbors(temp.data.id)) {
                    if (!this.buildings.contains(getLocationByID(id)) || start.id == id || target.id == id) {
                        if (priority.containsKey(id)) {
                            if (priority.get(id) > temp.priority + adjacent(temp.data.id, id)) {
                                parent.put(id, temp.data.id);
                                priority.put(id, temp.priority + adjacent(temp.data.id, id));
                                lst.decreaseKey(new IPriorityQueue.PQElement<>(getLocationByID(id), temp.priority + adjacent(temp.data.id, id)));
                            } else {
                                priority.put(id, temp.priority + adjacent(temp.data.id, id));
                                parent.put(id, temp.data.id);
                                IPriorityQueue.PQElement<Location> other = new IPriorityQueue.PQElement<>(getLocationByID(id), temp.priority + adjacent(temp.data.id, id));
                                lst.enqueue(other);
                            }
                        }
                    }
                }
            }
        }
        boolean finished = false;
        long current = target.id;
        fin.addBack(getLocationByID(current));
        while(!finished){
            if(current == start.id){
                finished = true;
            }else{
                if(parent.get(current) == null){
                    return null;
                }
                current = parent.get(current);
                fin.addFront(getLocationByID(current));
            }

        }
        return fin;
    }

    /**
     * Returns a JsonElement corresponding to the data in the file
     * with the filename filename
     * @param filename the name of the file to return the data from
     * @return the JSON data from filename
     */
    private static JsonElement fromFile(String filename) {
        try {
            return JSON_PARSER.parse(
                    new FileReader(
                            new File(filename)
                    )
            );
        } catch (IOException e) {
            return null;
        }
    }
}
