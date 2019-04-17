package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;
import bearmaps.lab9.MyTrieSet;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private Map<Point, Long> pointToID;
    private KDTree kdTree;
    private MyTrieSet Trie;
    private Map<String, String> cleanedToFull;
    private Map<String, Long> nameToID;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        // List<Node> nodes = this.getNodes();
        pointToID = new HashMap<>();
        List<Point> points = new ArrayList<>();
        List<Node> nodes = this.getNodes();
        Trie = new MyTrieSet();
        cleanedToFull = new HashMap<>();
        nameToID = new HashMap<>();

        for (Node node : nodes) {
            long id = node.id();

            // If the node has a name, clean it, then add it to the Trie,
            // and put the (cleaned name, full name) pair into the cleanedToFull map,
            // and put the (full name, id) pair into nameToID map.
            if (this.name(id) != null) {
                String fullName = this.name(id);
                String cleanedName = cleanString(fullName);

                nameToID.put(fullName, id);
                cleanedToFull.put(cleanedName, fullName);
                Trie.add(cleanedName);
            }

            // Only consider the node that has neighbors,
            // and turn these nodes to Points to service
            // the KDTree.
            if (!this.neighbors(id).isEmpty()) {
                double x = node.lon();
                double y = node.lat();
                Point point = new Point(x, y);

                points.add(point);
                pointToID.put(point, id);
            }
        }

        kdTree = new KDTree(points);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point closestPoint = kdTree.nearest(lon, lat);
        return pointToID.get(closestPoint);
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> locations = new ArrayList<>();
        String cleanedPrefix = cleanString(prefix);
        List<String> matchedNames = Trie.keysWithPrefix(cleanedPrefix);

        for (String name : matchedNames) {
            String fullMatched = cleanedToFull.get(name);

            locations.add(fullMatched);
        }

        return locations;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> locations = new ArrayList<>();
        Map<String, Object> locationInfo = new HashMap<>();
        String cleanedLocationName = cleanString(locationName);

        // Return an empty list if no location name matches the locationName.
        if (!cleanedToFull.containsKey(cleanedLocationName)) {
            return new ArrayList<>();
        }

        String name = cleanedToFull.get(cleanedLocationName);
        locationInfo.put("name", name);

        long id = nameToID.get(name);
        locationInfo.put("id", id);

        double lon = this.lon(id);
        locationInfo.put("lon", lon);

        double lat = this.lat(id);
        locationInfo.put("lat", lat);

        locations.add(locationInfo);

        return locations;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
