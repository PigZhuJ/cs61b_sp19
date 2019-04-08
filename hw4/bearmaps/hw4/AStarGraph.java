package bearmaps.hw4;

import java.util.List;

/**
 * Represents a graph of vertices.
 * Created by hug.
 */
public interface AStarGraph<Vertex> {
    /**
     * Provides a list of all edges that go out from v to its neighbors.
     *
     * @param v
     * @return
     */
    List<WeightedEdge<Vertex>> neighbors(Vertex v);

    /**
     * Provides an estimate of the number of moves to reach the goal from
     * the start position. For results to be correct, this estimate must
     * be less than or equal to the correct distance.
     *
     * @param s
     * @param goal
     * @return
     */
    double estimatedDistanceToGoal(Vertex s, Vertex goal);
}
