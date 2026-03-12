package lab11.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private Queue<Integer> fringe;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        fringe = new ArrayDeque<>();
        mark(s);
        fringe.add(s);
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        while (!fringe.isEmpty()) {
            Integer v = fringe.remove();
            if (v.equals(t)) {
                return;
            }
            for (int i : maze.adj(v)) {
                if (marked[i]) {
                    continue;
                }
                mark(i);
                edgeTo[i] = v;
                distTo[i] = distTo[v] + 1;
                announce();
                fringe.add(i);
            }
        }

    }

    private void mark(int v) {
        marked[v] = true;
    }


    @Override
    public void solve() {
        bfs();
    }
}

