import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Graph<T> {

    private LinkedList<Integer>[] adjacencyList; /** global variables **/
    ArrayList<String> actors;

    public Graph(int vertice, ArrayList<String> actors) { /** graph function in order connect the vertice and the actors together **/
        this.actors = actors;
        adjacencyList = new LinkedList[vertice];
        for (int i = 0; i < vertice; i++) {
            adjacencyList[i] = new LinkedList<>();
        }
    }

    public void add(int a1, int a2) { /** add the actors to the graph **/
        if (a1 != a2 && !adjacencyList[a1].contains(a2) && !adjacencyList[a2].contains(a1)) {
            adjacencyList[a1].add(a2);
            adjacencyList[a2].add(a1);
        }
    }

    public ArrayList<Integer> reconPath(int start, int target, int[] prev) { /** reconstruction of the path that finds the path from start to current **/
        ArrayList<Integer> path = new ArrayList<>();
        for (int i = target; i != -1; i=prev[i]) {
            if (prev[i] != -1) {
                path.add(i);
            }
        }
        Collections.reverse(path);

        if (path.size() > 0 && path.get(path.size()-1) == target) {
            return path;
        }
        return null;
    }

    public ArrayList<Integer> shortestPath(int start, int end) { /** shortest path between the two actors on the graph **/
        int[] previous = findPath(start); /** previous becomes the start of the new path identified **/
        return reconPath(start, end, previous);
    }

    public int[] findPath(int start) {
        Queue queue = new Queue();
        queue.Enqueue(start);
        boolean[] visitedNodes = new boolean[adjacencyList.length];
        for (int i = 0; i < visitedNodes.length; i++) {
            visitedNodes[i] = false;
        }
        visitedNodes[start] = true;
        int[] prev = new int[adjacencyList.length];
        for (int i = 0; i < prev.length; i++) {
            prev[i] = -1;
        }
        int curr;
        LinkedList<Integer> neighbors;
        while(!queue.Empty()) {
            curr = queue.Dequeue();
            neighbors = adjacencyList[curr];
            for (int next : neighbors) {
                if (!visitedNodes[next]) {
                    prev[next] = curr;
                    queue.Enqueue(next);
                    visitedNodes[next] = true;
                }
            }
        }
        return prev;
    }
}
