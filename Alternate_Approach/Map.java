import java.util.ArrayList;

public class Map {

    private ArrayList<Node> nodes;
    private ArrayList<Virus> viruses;
    private ArrayList<Node[]> adjacentNodes;

    public Map() {
        nodes = new ArrayList<>();
        viruses = new ArrayList<>();
        adjacentNodes = new ArrayList<>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void removeNode(Node node) {
        nodes.remove(node);
    }

    public void addVirus(Virus virus) {
        viruses.add(virus);
        for(Node node : nodes) {
            if(virus.getAttackNode().equals(node.getName())) {
                node.getAttacked(virus, this);
            }
        }
    }

    public void addAdjacentNodes(String str1, String str2) {
        Node[] nodeArray = new Node[2];
        for(Node node : nodes) {
            if(node.getName().equals(str1))
                nodeArray[0] = node;
            else if(node.getName().equals(str2))
                nodeArray[1] = node;
        }
        adjacentNodes.add(nodeArray);
    }

    public void nodeGoneInactive(Node inactiveNode) {
        adjacentNodes.removeIf(nodeArray -> nodeArray[0].getName().equals(inactiveNode.getName()));
        adjacentNodes.removeIf(nodeArray -> nodeArray[1].getName().equals(inactiveNode.getName()));
    }

    public ArrayList<String> getActiveNodes() {
        ArrayList<String> activeNodes = new ArrayList<>();
        for(Node node : nodes) {
            if(node.getStatus())
                activeNodes.add(node.getName());
        }
        return activeNodes;
    }

    public ArrayList<String> getInactiveNodes() {
        ArrayList<String> activeNodes = new ArrayList<>();
        for(Node node : nodes) {
            if(!node.getStatus())
                activeNodes.add(node.getName());
        }
        return activeNodes;
    }

    public void printInfectedNodes() {
        ArrayList<String> infectedNode = new ArrayList<>();
        for(Node node : nodes) {
            if(node.getStatus() && !node.getFirewall() && (node.getVirusAttacks().size() > 0)) {
                infectedNode.add(node.getName());
            }
        }

        System.out.println("There are " + infectedNode.size() + " infected nodes.");
        if(infectedNode.size() > 0) {
            System.out.println("They are: ");
            for(String s : infectedNode) {
                System.out.println(s);
            }
        }
    }

    public void printFirewallNodes() {
        ArrayList<String> firewallNode = new ArrayList<>();
        for (Node node : nodes) {
            if (node.getStatus() && node.getFirewall()) {
                firewallNode.add(node.getName());
            }
        }

        System.out.println("There are " + firewallNode.size() + " nodes with firewalls.");
        if(firewallNode.size() > 0) {
            System.out.println("They are: ");
            for(String s : firewallNode) {
                System.out.println(s);
            }
        }

    }
}
