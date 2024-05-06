import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test {

    private static final String SEPARATOR = "-------------------------------------";
    public static Map map = new Map();

    public static void main(String[] args) throws FileNotFoundException {
        boolean adding = true;
        Scanner graph = new Scanner(new File("Graph.txt"));
        while(graph.hasNextLine()) {
            String graphLine = graph.nextLine();
            if (graphLine.equals(SEPARATOR)) {
                adding = false;
                graphLine = graph.nextLine();
            }

            String[] graphStr = graphLine.split(", ");
            if(adding) {
                if (graphStr.length == 4) {
                    map.addNode(new Node(graphStr[0], new double[]{Double.parseDouble(graphStr[1].substring(1)), Double.parseDouble(graphStr[2].substring(0, graphStr[2].length() - 1))}, true));
                } else if (graphStr.length == 3)
                    map.addNode(new Node(graphStr[0], new double[]{Double.parseDouble(graphStr[1].substring(1)), Double.parseDouble(graphStr[2].substring(0, graphStr[2].length() - 1))}, false));
            }
            else {
                map.addAdjacentNodes(graphStr[0], graphStr[1]);
            }
        }

        Scanner attack = new Scanner(new File("Attack.txt"));
        while(attack.hasNextLine()) {
            String attackLine = attack.nextLine();
            String[] attackStr = attackLine.split(", ");
            map.addVirus(new Virus(attackStr[0], attackStr[1], attackStr[2], attackStr[3]));
        }

        map.printInfectedNodes();
        System.out.println();
        map.printFirewallNodes();
    }
}
