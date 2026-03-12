package lab11.graphs;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    PriorityQueue<Node> PQ;

    private class Node {
        int key;
        int value;

        Node(int k, int v) {
            key = k;
            value = v;
        }
    }

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        PQ = new PriorityQueue<>(Comparator.comparingInt(u -> u.value));
        PQ.add(new Node(s, 0));
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        marked[s] = true;
        // TODO
        while (!PQ.isEmpty()) {
            Node v = PQ.remove();
            if (v.key == t) {
                return;
            }
            for (int i : maze.adj(v.key)) {
                if (!marked[i]) {
                    marked[i] = true;
                    relax(v.key, i);
                    announce();
                }
            }
        }
    }

    //Relax operation of Dijkstra's algorithm.
    private void relax(int p, int q) {
        if (distTo[p] + 1 < distTo[q]) {
            distTo[q] = distTo[p] + 1;
            edgeTo[q] = p;
            PQ.add(new Node(q, distTo[q] + h(q)));
        }
    }
    @Override
    public void solve() {
        astar(s);
    }
}
