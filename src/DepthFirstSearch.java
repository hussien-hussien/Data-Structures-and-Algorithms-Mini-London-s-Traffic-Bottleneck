import java.util.Iterator;
import java.util.Stack;

public class DepthFirstSearch {
    private RouteGraph inputGraph;
    private Stack<Intersection> stack;


    public DepthFirstSearch(RouteGraph graph){
        this.inputGraph = graph;
        this.stack = new Stack<>();
    };

    public Stack<Intersection> path(Intersection startVertex, Intersection endVertex){
        this.stack = new Stack<>();

        // add throws GraphException

        pathRec(startVertex,endVertex);

        return stack;
    }


    public void pathRec(Intersection startVertex, Intersection endVertex){
        // Implement the algorithm (called path) given in class for computing a path via a depth-first search.
        // You must implement this algorithm; it will not find shortest paths, nor paths using the least
        // number of edges, we will assume the drivers in Mini-London might like to go for long drives.
        // This recursive method when completed must have the path stored in stack. Tip: You might
        // find the method incidentRoads useful from the RouteGraph class, also remember that the
        // edges are undirected (the order of the endpoints for edges are irrelevant).
        // You are not responsible for de-marking the vertices,
        // the class RouteFind will take care of removing the markings on vertices.

        if (startVertex.getMark()){return;}
        // mark currentNode as visited
        startVertex.setMark(true);


        if (startVertex == endVertex){
            stack.push(startVertex);
            return;
        }
        Iterator<Road> roads = null;
        //fix this!
        try {
            roads = inputGraph.incidentRoads(startVertex);
        } catch (GraphException e) {
            e.printStackTrace();
        }

        // loop through children of startVertex using iterator
        while (roads.hasNext()) {
            Intersection v = roads.next().getSecondEndpoint();
            pathRec(v,endVertex);

            // pathrec on each

            if (!stack.empty() && v == stack.peek()){
                stack.push(startVertex);
                return;
            }
        }
    }

}
