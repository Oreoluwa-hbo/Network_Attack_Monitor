import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class RouteSearch {


    private List<List<Node>> allPaths = new ArrayList<>();

    public void search(List<Node> path, Node start, Node end) {

        path.add(start);
        if (start == end && path.size() > 1) {
            System.out.println(path);
            // because the path variable is recursively used, we need to create copies of it anytime a complete path is found
            // otherwise all saved paths would end up having the same manipulated value
            allPaths.add(new ArrayList<>(path));
        } else {
            for (Node n : start.neighbors) {
                // only active neighbors without attacks can be included in path. This includes nodes with firewalls that blocked attacks
                if ((n.status.equals("active")&&n.hasFirewall) || (!n.hasFirewall && n.status.equals("active")&&n.attacks.size()==0)) {
                    if (!path.contains(n))
                        search(path, n, end); // the recursion happens here
               }
            }
        }
        // after a recursion down a neighbor's path ends, we need to backtrack by deleting from the last elements in the path till we reach the branching point.
        // This allows to try another neighbor path without including any of the nodes in the previous neighbor's path
        path.remove(path.size() - 1);
    }

    public static double calculatePathDistance(List<Node> path) {
        double totalDistance = 0;

        for (int i = 0; i < path.size()-1; i++) {
            totalDistance = totalDistance + calculateDistanceBetweenNodes(path.get(i), path.get(i + 1));
        }

        return totalDistance;
    }

    private static double calculateDistanceBetweenNodes(Node a, Node b) {
        return Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }

    public List<List<Node>> getAllPaths() {
        return allPaths;
    }
}
