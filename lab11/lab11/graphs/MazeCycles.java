package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean hasCycle = false;

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        // TODO: Your code here!
        mark(0);
        dfs(0);
    }

    private void dfs(int s) {
        for (int u : maze.adj(s)) {
            if (hasMark(u)) {
                if (u != edgeTo[s]) {
                    hasCycle = true;
                    changeEdgeTo(u);
                } else {
                    continue;
                }
            }
            if (hasCycle) {
                edgeTo[u] = s;
                announce();
                return;
            } else {
                edgeTo[u] = s;
                mark(u);
            }
            announce();
            dfs(u);
        }
    }

    // Helper methods go here
    private void mark(int v) {
        marked[v] = true;
    }

    private boolean hasMark(int v) {
        return marked[v];
    }
   /*
   Remove all edges that connect to vertex u.
    */
    private void changeEdgeTo(int u) {
        int i = u;
        while (i != 0) {
            int tmp = edgeTo[i];
            edgeTo[i] = Integer.MAX_VALUE;
            i = tmp;
        }
    }
}
