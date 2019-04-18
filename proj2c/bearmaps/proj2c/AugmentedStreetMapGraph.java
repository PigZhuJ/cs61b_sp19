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

    private MyTrieSet trieSet;
    private Map<String, List<Node>> cleanedNameToNodes;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        // List<Node> nodes = this.getNodes();
        pointToID = new HashMap<>();
        List<Point> points = new LinkedList<>();
        List<Node> nodes = this.getNodes();

        trieSet = new MyTrieSet();
        cleanedNameToNodes = new HashMap<>();
        List<Node> nodesList;

        for (Node node : nodes) {
            // If the node has a name, clean it, then add it to the trieSet,
            // and put the (cleaned name, list of nodes) pair into the cleanedNameToNodes map.
            if (node.name() != null) {
                String cleanedName = cleanString(node.name());

                trieSet.add(cleanedName);

                if (!cleanedNameToNodes.containsKey(cleanedName)) {
                    cleanedNameToNodes.put(cleanedName, new LinkedList<>());
                }
                // Replace the old list that mapped to the specific cleanedName with new one.
                nodesList = cleanedNameToNodes.get(cleanedName);
                nodesList.add(node);
                cleanedNameToNodes.put(cleanedName, nodesList);
            }

            // Only consider the node that has neighbors,
            // and turn these nodes to Points to service
            // the KDTree.
            long id = node.id();
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
     * @param prefix Prefix string to be searched for. Could be any case, with or without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        String cleanedPrefix = cleanString(prefix);
        List<String> matchedNames = trieSet.keysWithPrefix(cleanedPrefix);
        Set<String> locationsSet = new HashSet<>();

        for (String name : matchedNames) {
            for (Node node : cleanedNameToNodes.get(name)) {
                locationsSet.add(node.name());
            }
        }

        return new LinkedList<>(locationsSet);
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
        List<Map<String, Object>> locations = new LinkedList<>();
        String cleanedLocationName = cleanString(locationName);

        if (cleanedNameToNodes.containsKey(cleanedLocationName)) {
            for (Node node : cleanedNameToNodes.get(cleanedLocationName)) {
                Map<String, Object> locationInfo = new HashMap<>();
                locationInfo.put("id", node.id());
                locationInfo.put("name", node.name());
                locationInfo.put("lon", node.lon());
                locationInfo.put("lat", node.lat());
                locations.add(locationInfo);
            }
        }

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
